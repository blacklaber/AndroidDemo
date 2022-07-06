package com.example.fragmentbestpractice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment

class NewsContentFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.news_content_frag,container,false
        )
    }

    fun refresh(title: String,content: String) {
        val contentLayout: LinearLayout = view?.findViewById(R.id.contentLayout) as LinearLayout
        val newsTitle: TextView = view?.findViewById(R.id.newsTitle) as TextView
        val newsContent: TextView = view?.findViewById(R.id.newsContent) as TextView

        contentLayout.visibility = View.VISIBLE
        newsTitle.text = title
        newsContent.text = content

    }
}