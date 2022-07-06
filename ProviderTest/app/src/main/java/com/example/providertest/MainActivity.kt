package com.example.providertest

import android.annotation.SuppressLint
import android.content.ContentValues
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.content.contentValuesOf
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    var bookId: String? = null

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addData = findViewById<Button>(R.id.addData)
        val updateData = findViewById<Button>(R.id.updateData)
        val deleteData = findViewById<Button>(R.id.deleteData)
        val queryData = findViewById<Button>(R.id.queryData)


        addData.setOnClickListener {
            val uri = Uri.parse("content://com.example.databasetest.provider/book")
            val values = contentValuesOf("name" to "A Clash of Kings","author" to "Bob","pages" to 1009,"price" to 29.9)
            val newUri = contentResolver.insert(uri,values)
            bookId = newUri?.pathSegments?.get(1)
        }

        queryData.setOnClickListener {
            val uri = Uri.parse("content://com.example.databasetest.provider/book")
            contentResolver.query(uri,null,null,null,null)?.apply {
                while (moveToNext()){
                    val name = getString(getColumnIndex("name"))
                    val author = getString(getColumnIndex("author"))
                    val pages = getInt(getColumnIndex("pages"))
                    val price = getDouble(getColumnIndex("price"))
                    Log.d("MainActivity","name is $name")
                    Log.d("MainActivity","author is $author")
                    Log.d("MainActivity","pages is $pages")
                    Log.d("MainActivity","price is $price")
                }
                close()
            }
        }

        updateData.setOnClickListener {
            bookId?.let {
                val uri = Uri.parse("content://com.example.databasetest.provider/book/$it")
                val values = contentValuesOf("name" to "Bye bye","author" to "Tom","pages" to 97,"price" to 88)
                contentResolver.update(uri,values,null,null)
            }

        }

        deleteData.setOnClickListener {
            bookId?.let{
                val uri = Uri.parse("content://com.example.databasetest.provider/book/$it")
                contentResolver.delete(uri,null,null)
            }
        }

    }
}