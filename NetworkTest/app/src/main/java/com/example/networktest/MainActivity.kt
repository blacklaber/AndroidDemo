package com.example.networktest

import HttpCallbackListener
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import org.json.JSONArray
import org.xml.sax.Attributes
import org.xml.sax.InputSource
import org.xml.sax.helpers.DefaultHandler
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.StringReader
import java.net.HttpURLConnection
import java.net.URL
import javax.xml.parsers.SAXParserFactory
import kotlin.concurrent.thread

class ContentHandler : DefaultHandler(){

    private var nodeName = ""
    private lateinit var id: StringBuilder
    private lateinit var name: StringBuilder
    private lateinit var version: StringBuilder

    override fun startDocument() {
        id = StringBuilder()
        name = StringBuilder()
        version = StringBuilder()
        super.startDocument()
    }

    override fun endDocument() {
        super.endDocument()
    }

    override fun startElement(
        uri: String?,
        localName: String?,
        qName: String?,
        attributes: Attributes?,
    ) {
        if (localName != null) {
            nodeName = localName
        }
        Log.d("ContentHandler","uri is $uri")
        Log.d("ContentHandler","localName is $localName")
        Log.d("ContentHandler","qName is $qName")
        Log.d("ContentHandler","attributes is $attributes")

    }

    override fun endElement(uri: String?, localName: String?, qName: String?) {
        if ("app" == localName){
            Log.d("ContentHandler","id is ${id.toString().trim()}")
            Log.d("ContentHandler","name is ${name.toString().trim()}")
            Log.d("ContentHandler","version is ${version.toString().trim()}")

            id.setLength(0)
            name.setLength(0)
            version.setLength(0)
        }
    }

    override fun characters(ch: CharArray?, start: Int, length: Int) {
        when(nodeName){
            "id" -> id.append(ch,start,length)
            "name" -> name.append(ch,start,length)
            "version" -> version.append(ch,start,length)
        }
    }
}

class App(val id: String,val name: String,val version: String)

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sendRequestBtn = findViewById<Button>(R.id.sendRequestBtn)
//        sendRequestBtn.setOnClickListener {
//            sendRequestWithOkHttp()
//        }
        val address = "http://10.0.2.2:8081/get_data.json"
        sendRequestBtn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
//                HttpUtil.sendHttpRequest(address,object : HttpCallbackListener{
//                    override fun onFinish(response: String) {
//                        showResponse(response)
//                    }
//
//                    override fun onError(e: Exception) {
//                        e.printStackTrace()
//                    }
//
//                })
                HttpUtil.sendOkHttpRequest(address,object :Callback{
                    override fun onFailure(call: Call, e: IOException) {

                    }

                    override fun onResponse(call: Call, response: Response) {
                        val responseData = response.body?.string()
                        if (responseData != null){
                            parseJSONWithGSON(responseData)
                        }
                    }

                })

            }

        })
    }

    private fun sendRequestWithHttpURLConnection() {
        thread {
            var connection: HttpURLConnection? = null
            try {
                val response = StringBuilder()
                val url = URL("https://www.baidu.com")
                connection = url.openConnection() as HttpURLConnection
                connection.connectTimeout = 1000
                connection.readTimeout = 1000
                val input = connection.inputStream
                val reader = BufferedReader(InputStreamReader(input))
                reader.use {
                    reader.forEachLine {
                        response.append(it)
                    }
                }
                showResponse(response.toString())
            }catch (e:Exception){
                e.printStackTrace()
            }finally {
                connection?.disconnect()
            }
        }

    }

    private fun sendRequestWithOkHttp() {
        thread {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("http://10.0.2.2:8081/get_data.json")
                    .build()
                val response = client.newCall(request).execute()
                val responseData = response.body?.string()
                if (responseData != null){
                    parseJSONWithGSON(responseData)
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    private fun parseJSONWithGSON(jsonData: String) {
        val gson = Gson()
        val typeOf = object : TypeToken<List<App>>() {}.type
        val appList = gson.fromJson<List<App>>(jsonData,typeOf)
        for (app in appList){
            Log.d("MainActivity","id is ${app.id}")
            Log.d("MainActivity","name is ${app.name}")
            Log.d("MainActivity","version is ${app.version}")
        }
    }

    private fun parseJSONWithJSONObject(jsonData: String) {
        try {
            val jsonArray = JSONArray(jsonData)
            for (i in 0 until jsonArray.length()){
                val jsonObject = jsonArray.getJSONObject(i)
                val id = jsonObject.getString("id")
                val version = jsonObject.getString("version")
                val name = jsonObject.getString("name")
                Log.d("MainActivity","id is $id")
                Log.d("MainActivity","name is $name")
                Log.d("MainActivity","version is $version")
            }
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    private fun parseXMLWithPull(xmlData: String) {
        try {
            val factory = XmlPullParserFactory.newInstance()
            val xmlPullParser = factory.newPullParser()
            xmlPullParser.setInput(StringReader(xmlData))
            var eventType = xmlPullParser.eventType
            var id = ""
            var name = ""
            var version =  ""
            while (eventType != XmlPullParser.END_DOCUMENT){
                val nodeName = xmlPullParser.name
                when(eventType){
                    XmlPullParser.START_TAG ->{
                        when(nodeName){
                            "id" -> id = xmlPullParser.nextText()
                            "name" -> name = xmlPullParser.nextText()
                            "version" -> version = xmlPullParser.nextText()
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if ("app" == nodeName) {
                            Log.d("MainActivity","id is $id")
                            Log.d("MainActivity","name is $name")
                            Log.d("MainActivity","version is $version")
                        }
                    }
                }
                eventType = xmlPullParser.next()
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun parseXMLWithSAX(xmlData: String) {
        try {
            val factory = SAXParserFactory.newInstance()
            val xmlReader = factory.newSAXParser().xmlReader
            val handler = ContentHandler()
            xmlReader.contentHandler = handler
            xmlReader.parse(InputSource(StringReader(xmlData)))
        }catch (e:Exception){
            e.printStackTrace()
        }
    }


    private fun showResponse(response: String) {
        runOnUiThread {
            val responseText = findViewById<TextView>(R.id.responseText)
            responseText.text = response
        }
    }
}