package com.example.servicetest

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button

class MainActivity : AppCompatActivity() {

    lateinit var downloadBinder: MyService.DownloadBinder

    private val connection = object : ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            downloadBinder = service as MyService.DownloadBinder
            downloadBinder.startDownload()
            downloadBinder.getProgress()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            TODO("Not yet implemented")
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startServiceBtn = findViewById<Button>(R.id.startServiceBtn);
        val stopServiceBtn = findViewById<Button>(R.id.stopServiceBtn);

        val bindServiceBtn = findViewById<Button>(R.id.bindServiceBtn);
        val unbindServiceBtn = findViewById<Button>(R.id.unbindServiceBtn);

        val startIntentServiceBtn = findViewById<Button>(R.id.startIntentServiceBtn);

        startServiceBtn.setOnClickListener {
            val intent = Intent(this,MyService::class.java)
            startService(intent)
        }

        stopServiceBtn.setOnClickListener {
            val intent = Intent(this,MyService::class.java)
            stopService(intent)
        }


        bindServiceBtn.setOnClickListener {
            val intent = Intent(this,MyService::class.java)
            bindService(intent,connection,Context.BIND_AUTO_CREATE)
        }

        unbindServiceBtn.setOnClickListener {
            unbindService(connection)
        }

        startIntentServiceBtn.setOnClickListener {
            Log.d("MainActivity","Thread id is ${Thread.currentThread().name}$")
            val intent = Intent(this,MyIntentService::class.java)
            startService(intent)
        }
    }
}