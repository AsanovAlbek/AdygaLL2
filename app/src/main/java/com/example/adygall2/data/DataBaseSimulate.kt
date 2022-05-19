package com.example.adygall2.data

import com.example.adygall2.R
import com.example.adygall2.models.ImageTask

object DataBaseSimulate {
    val ImageAnswerList = listOf(
        ImageTask(
            0, "Шы", "Конь",
            R.drawable.horse, QuestionType.FOUR_IMAGE_QUESTION
        ),

        ImageTask(
            0, "Пухирi", "Пузыри",
            R.drawable.bubbles, QuestionType.FOUR_IMAGE_QUESTION
        ),

        ImageTask(
            0, "Мазэ", "Луна",
            R.drawable.moon, QuestionType.FOUR_IMAGE_QUESTION
        ),

        ImageTask(
            0, "Унэ", "Дом",
            R.drawable.house, QuestionType.FOUR_IMAGE_QUESTION
        )
    )

    val simpleWordsAnswerList = listOf(
        "Шы", "Тхыль", "Мазэ"
    )

    val fakeWords = listOf(
        "о", "сэ", "шы", "цэ", "фыжьы", "мазэ", "унэ", "псы"
    )
}