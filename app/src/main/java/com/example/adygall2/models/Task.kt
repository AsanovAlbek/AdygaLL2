package com.example.adygall2.models

abstract class Task {
    abstract val number : Int
    abstract fun isCurrentAnswer(answer : String) : Boolean
}