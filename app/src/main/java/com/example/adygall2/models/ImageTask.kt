package com.example.adygall2.models

import com.example.adygall2.data.QuestionType

class ImageTask(
    val id : Int,
    val firstLangWord : String,
    val secondLangWord : String,
    val image : Int,
    val questionType : QuestionType
) : Task() {
    override val number = id
    override fun isCurrentAnswer(answer : String) : Boolean = answer.compareTo(firstLangWord) == 0
}