package com.example.activitylifecycletest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class NormalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_normal)
        val bundle: Bundle? = intent.getBundleExtra("bundle1")

        bundle?.getString("test_data")?.let { Log.d("test", it) }

    }
}