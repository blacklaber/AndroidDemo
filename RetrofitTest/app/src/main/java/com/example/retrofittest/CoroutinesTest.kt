package com.example.retrofittest

import android.location.Address
import android.util.Log
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.RuntimeException
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

fun main(){
//    val start = System.currentTimeMillis()
    runBlocking {
        val start = System.currentTimeMillis()

//        val result1 = async {
//            delay(1000)
//            5+1
//        }
//
//        val result2 = async {
//            delay(1000)
//            5+9
//        }
//
//        println(result1.await()+result2.await())

        val result = withContext(Dispatchers.Default){
            9+9
        }
        println(result)
        val end = System.currentTimeMillis()
        print(end - start)
    }

}
suspend fun printDot(){
    coroutineScope {
        launch {
            println(".")
            delay(1000)
        }
    }
}

interface HttpCallbackListener{
    fun onFinish(response: String)
    fun onError(e: Exception)
}

object HttpUtil {

    fun sendHttpRequest(address: String,listener: HttpCallbackListener){
        thread {
            var connection: HttpURLConnection? = null
            try {
                val response = StringBuilder()
                val url = URL(address)
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
                listener.onFinish(response.toString())
            }catch (e:Exception){
                e.printStackTrace()
                listener.onError(e)
            }finally {
                connection?.disconnect()
            }

        }
    }

    fun sendOkHttpRequest(address: String,callback:okhttp3.Callback){
        try {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(address)
                .build()
            client.newCall(request).enqueue(callback)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}

suspend fun <T> Call<T>.await():T{
    return suspendCoroutine {
            continuation ->
        enqueue(object : Callback<T>{
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val body = response.body()
                if (body != null){
                    continuation.resume(body)
                }else{
                    continuation.resumeWithException(
                        RuntimeException("empty")
                    )
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                continuation.resumeWithException(t)
            }
        })
    }
}