package com.example.coroutinestest

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun main(){
    GlobalScope.launch {
        println("codes run in coroutine scope")
    }
}