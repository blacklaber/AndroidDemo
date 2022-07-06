package com.example.uicustomactivity

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*

class TitleLayout(context: Context,attrs: AttributeSet) : LinearLayout(context,attrs) {
    init {
        LayoutInflater.from(context).inflate(R.layout.title,this)
        val titleBack: Button = findViewById(R.id.titleBack)
        /*
        titleBack.setOnClickListener {
            val activity = context as Activity
            activity.finish()
        }

         */
        val titleEdit: Button  = findViewById(R.id.titleEdit)
        /*
        titleEdit.setOnClickListener {
            Toast.makeText(context,"You clicked the edit Button",Toast.LENGTH_SHORT).show()
        }

         */

        titleEdit.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                val toast:Toast = Toast.makeText(context,"You clicked the edit Button",Toast.LENGTH_SHORT)
                toast.show()
            }

        })


        titleBack.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                val activity = context as Activity
                activity.finish()
            }
        })

    }
}