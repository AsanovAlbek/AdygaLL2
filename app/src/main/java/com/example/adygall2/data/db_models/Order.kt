package com.example.adygall2.data.db_models

data class Order(
    val id : Int,

    val taskNum : Int
) {
    fun logInfo() : String =
        StringBuilder().apply {
            append("Order: id = $id, task_num = $taskNum")
        }.toString()
}