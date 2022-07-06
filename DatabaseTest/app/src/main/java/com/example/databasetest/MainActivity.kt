package com.example.databasetest

import android.annotation.SuppressLint
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.content.contentValuesOf
import java.lang.Exception
import java.lang.NullPointerException

class MainActivity : AppCompatActivity() {
    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val createDatabase = findViewById<Button>(R.id.createDatabase)
        val addData = findViewById<Button>(R.id.addData)
        val updateData = findViewById<Button>(R.id.updateData)
        val deleteData = findViewById<Button>(R.id.deleteData)
        val queryData = findViewById<Button>(R.id.queryData)
        val replaceData = findViewById<Button>(R.id.replaceData)


        val dbHelper = MyDatabaseHelper(this,"BookStore.db",2)
        createDatabase.setOnClickListener {
            dbHelper.writableDatabase
        }

        addData.setOnClickListener {
            val db = dbHelper.writableDatabase
            val values1 = ContentValues().apply {
                put("name","David")
                put("author","Dan")
                put("pages",100)
                put("price",19.99)
            }
            db.insert("Book",null,values1)
            val values2 = ContentValues().apply {
                put("name","The Lost")
                put("author","Dan")
                put("pages",219)
                put("price",29.99)
            }
            db.insert("Book",null,values2)

            val values3 = cvOf("name" to "DONG","author" to "Geroge","pages" to 80,"price" to 20.99)
            db.insert("Book",null,values3)
            val values4 = contentValuesOf("name" to "DONG","author" to "Geroge","pages" to 80,"price" to 20.99)
            db.insert("Book",null,values3)
        }

        updateData.setOnClickListener {
            val db = dbHelper.writableDatabase
            val values = ContentValues()
            values.put("price",99.99)
            db.update("Book",values,"name = ?", arrayOf("David"))
        }

        deleteData.setOnClickListener {
            val db = dbHelper.writableDatabase
            db.delete("Book","price > ?", arrayOf("80"))
        }

        queryData.setOnClickListener {
            val db = dbHelper.writableDatabase
            val cursor = db.query("Book",null,null,null,null,null,null)
            if (cursor.moveToFirst()){
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val author = cursor.getString(cursor.getColumnIndex("author"))
                val pages = cursor.getInt(cursor.getColumnIndex("pages"))
                Log.d("MainActivity",name)
                Log.d("MainActivity",author)
                Log.d("MainActivity",pages.toString())
            }
            cursor.close()
        }

        replaceData.setOnClickListener {
            val db = dbHelper.writableDatabase
            db.beginTransaction()
            try {
                db.delete("Book",null,null)
                if (true){
                    //throw NullPointerException()
                }
                val values = ContentValues().apply {
                    put("name","Game")
                    put("author","Bob")
                    put("pages",753)
                    put("price",39.87)
                }
                db.insert("Book",null,values)
                db.setTransactionSuccessful()
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                db.endTransaction()
            }
        }

    }
}