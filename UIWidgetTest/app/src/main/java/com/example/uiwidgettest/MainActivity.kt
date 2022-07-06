package com.example.uiwidgettest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import kotlin.math.log

class MainActivity : AppCompatActivity(),View.OnClickListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button1 : Button = findViewById(R.id.button)
        button1.setOnClickListener (this)
    }

    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.button -> {
                val ad: AlertDialog.Builder = AlertDialog.Builder(this)
                ad.setTitle("dialog")
                ad.setMessage("Something important.")
                ad.setCancelable(false)
                ad.setPositiveButton("OK"){dialog,which -> Log.d("ok","ok")}
                ad.setNegativeButton("Cancel"){dialog,which -> Log.d("cancal","cancel")}
                ad.show()
                /*
                AlertDialog.Builder(this).apply {
                    setTitle("dialog")
                    setMessage("Something important.")
                    setCancelable(false)
                    setPositiveButton("OK"){dialog,which ->}
                    setNegativeButton("Cancel"){dialog,which ->}
                    show()
                }
                */

            }
        }
    }


}