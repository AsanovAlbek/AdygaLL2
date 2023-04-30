package com.example.adygall2.presentation.model

data class GameState(
    val levelProgress: Int = 0,
    val lessonProgress: Int = 0,
    val levelName: String = "",
    val lessonTitle: String = "",
    val userAnswer: String = "",
    val rightAnswer: String = "",
    val startTime: Long = 0L,
    val finishTime: Long = 0L,
    val mistakesCounter: Int = 0,
    val mistakesCount: Int = 0,
    val newWordsCount: Int = 0,
    val currentQuestionPosition: Int = 0,
    val canSkipTask: Boolean = false,
    val coins: Int = 0,
    val hp: Int = 0
)