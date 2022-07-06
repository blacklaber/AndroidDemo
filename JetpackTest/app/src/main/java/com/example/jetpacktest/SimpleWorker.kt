package com.example.jetpacktest

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class SimpleWorker(context: Context,params: WorkerParameters) : Worker(context,params) {
    override fun doWork(): Result {
        println("do work in SimpleWorker")
        return Result.success()
    }
}