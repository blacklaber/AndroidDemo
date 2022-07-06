package com.example.sharedpreferencestes

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val saveButton = findViewById<Button>(R.id.saveButton)

        saveButton.setOnClickListener {
            getSharedPreferences("data", Context.MODE_PRIVATE).edit(false) {
                putString("name","Tom")
            }
        }

        val restoreButton = findViewById<Button>(R.id.restoreButton)

        restoreButton.setOnClickListener {
            val prefs = getSharedPreferences("data",Context.MODE_PRIVATE)
            val name = prefs.getString("name","")
            Toast.makeText(this, name, Toast.LENGTH_SHORT).show()
        }
    }
}