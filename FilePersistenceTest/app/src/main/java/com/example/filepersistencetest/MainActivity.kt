package com.example.filepersistencetest

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.EditText
import android.widget.Toast
import java.io.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val inputText = load()
        if (inputText.isNotEmpty()){
            val editText = findViewById<EditText>(R.id.edit_Text)
            editText.text = Editable.Factory.getInstance().newEditable(inputText)
            Toast.makeText(this, "Restrring succeed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val editText = findViewById<EditText>(R.id.edit_Text)
        val inputText = editText.text.toString()
        save(inputText)
    }
    
    private fun load() : String{
        val content = StringBuilder()
        try {
            val input = openFileInput("data")
            val reader = BufferedReader(InputStreamReader(input))
            reader.use { 
                reader.forEachLine { 
                    content.append(it)
                }
            }
        }catch (e: IOException){
            e.printStackTrace()
        }
        return content.toString()
    }

    private fun save(inputText: String){
        try {
            val output = openFileOutput("data", Context.MODE_PRIVATE)
            val writer = BufferedWriter(OutputStreamWriter(output))
            writer.use {
                it.write(inputText)
            }
        }catch (e:IOException){
            e.printStackTrace()
        }
    }
}