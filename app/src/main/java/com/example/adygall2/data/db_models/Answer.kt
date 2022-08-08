package com.example.adygall2.data.db_models

data class Answer(
    val id : Int,

    val taskId : Int,

    val answer : String,

    val correctAnswer : String,

    val pictureId : Int,

    val soundId : Int
) {
    constructor() : this(0, 0, "", "false", 0, 0)
}
