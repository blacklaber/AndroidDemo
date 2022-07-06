package com.example.jetpacktest

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Global.putInt
import android.widget.Button
import android.widget.TextView
import androidx.core.content.edit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    lateinit var sp: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycle.addObserver(MyObserver(lifecycle))

        sp = getPreferences(Context.MODE_PRIVATE)
        val countReserved = sp.getInt("count_reserved",0)
        viewModel= ViewModelProvider(this,MainViewModelFactory(countReserved)).get(MainViewModel::class.java)

        val plusButton = findViewById<Button>(R.id.plusButton)
        val clearButton = findViewById<Button>(R.id.clearButton)
        val getUserButton = findViewById<Button>(R.id.getUserButton)
        val addDataButton = findViewById<Button>(R.id.addDataButton)
        val updateDataButton = findViewById<Button>(R.id.updateDataButton)
        val deleteDataButton = findViewById<Button>(R.id.deleteDataButton)
        val queryDataButton = findViewById<Button>(R.id.queryDataButton)
        val doWorkButton = findViewById<Button>(R.id.doWorkButton)

        plusButton.setOnClickListener{
            viewModel.plusOne()
        }
        clearButton.setOnClickListener {
            viewModel.clear()
        }
        getUserButton.setOnClickListener {
            val userId = (0..1000).random().toString()
            viewModel.getUser(userId)
        }

        viewModel.counter.observe(this, object : Observer<Int>{
            override fun onChanged(t: Int?) {
                val infoText = findViewById<TextView>(R.id.infoText)
                infoText.text = t.toString()
            }
        })
        viewModel.user.observe(this,Observer{
            user ->
            val infoText = findViewById<TextView>(R.id.infoText)
            infoText.text = user.firstName
        })


//        viewModel.counter.observe(this, Observer {
//            count ->
//            val infoText = findViewById<TextView>(R.id.infoText)
//            infoText.text = count.toString()
//        })

        val userDao = AppDatabase.getDataBase(this).userDao()
        val user1 = User("Tom","Bob",40)
        val user2 = User("Tom2","Bob2",42)
        addDataButton.setOnClickListener {
            thread {
                user1.id = userDao.insertUser(user1)
                user2.id = userDao.insertUser(user2)
            }
        }

        updateDataButton.setOnClickListener {
            thread {
                user1.age = 41
                userDao.updateUser(user1)
            }
        }

        deleteDataButton.setOnClickListener {
            thread {
                userDao.deleteUserByLastName("Bob")
            }
        }

        queryDataButton.setOnClickListener {
            thread {
                for (user in userDao.loadAllUsers()) {
                    println(user.toString())
                }
            }
        }

        doWorkButton.setOnClickListener {
            val request = OneTimeWorkRequest.Builder(SimpleWorker::class.java)
                .setInitialDelay(5,TimeUnit.MINUTES)
                .addTag("simple")
                .build()
            WorkManager.getInstance(this).enqueue(request)

            WorkManager.getInstance(this)
                .getWorkInfoByIdLiveData(request.id)
                .observe(this){
                    workInfo ->
                    if (workInfo.state == WorkInfo.State.SUCCEEDED){
                        println("succeeded")
                    }else if (workInfo.state == WorkInfo.State.FAILED){
                        println("failed")
                    }
                }
        }
    }

    override fun onPause() {
        super.onPause()
        sp.edit{
            putInt("count_reserved",viewModel.counter.value ?: 0)
        }
    }

    private fun refreshCounter() {
//        val infoText = findViewById<TextView>(R.id.infoText)
//        infoText.text = viewModel.counter.toString()
    }
}