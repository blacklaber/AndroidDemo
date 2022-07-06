package com.example.fragmentbestpractice

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class NewsConrtentActivity : AppCompatActivity() {

    companion object{
        fun actionStart(context: Context,title: String,content: String) {
            val intent = Intent(context,NewsConrtentActivity::class.java).apply {
                putExtra("news_title",title)
                putExtra("news_content",content)
            }
            context.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_conrtent)

        val title = intent.getStringExtra("news_title")
        val content = intent.getStringExtra("news_content")

        if(title != null && content != null){
            val fragment = supportFragmentManager.findFragmentById(R.id.newsContentFrag) as NewsContentFragment
            fragment.refresh(title,content)
        }
    }
}