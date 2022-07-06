package com.example.helloworld

import android.util.Log


open class Person(var name: String) {
    init {
        println("MyTestOne-- MyTestOne init")
    }

    constructor(age: Int) : this("test") {
        println("MyTestOne--MyTestOne construct 1")
    }

    constructor(age: Int, name: String) : this(age) {
        println("MyTestOne--MyTestOne construct 2 $name")
    }
}