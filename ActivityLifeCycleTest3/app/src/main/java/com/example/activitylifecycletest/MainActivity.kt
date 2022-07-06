package com.example.activitylifecycletest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private val tag = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(tag,"OnCreate")
        setContentView(R.layout.activity_main)
        if (savedInstanceState != null)
        {
            val tempData = savedInstanceState.getString("data_key")
            Log.d(tag,"tempData is $tempData")
        }

        val startNormalActivity : Button = findViewById(R.id.startNormalActivity)
        val startDialogActivity : Button = findViewById(R.id.startDialogActivity)

        startNormalActivity.setOnClickListener {
            val intent = Intent(this,NormalActivity::class.java)
            val bundle: Bundle = Bundle()
            bundle.putString("test_data","data by test_data")
            intent.putExtra("bundle1",bundle)
            startActivity(intent)
        }

        startDialogActivity.setOnClickListener {
            val  intent = Intent(this,DialogActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val tempData = "Something you just typed"
        outState.putString("data_key",tempData)
    }




    override fun onStart() {
        super.onStart()
        Log.d(tag,"onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(tag,"onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(tag,"onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(tag,"onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(tag,"onDestroy")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(tag,"onRestart")
    }
}