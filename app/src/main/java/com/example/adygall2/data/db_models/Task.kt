package com.example.adygall2.data.db_models

data class Task(
    val id : Int,

    val taskType : Int,

    val task : String,

    val authorOfChanges : String,

    val dateOfChange : String
)
