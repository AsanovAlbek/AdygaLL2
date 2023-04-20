package com.example.adygall2.presentation.model

import android.graphics.Bitmap

data class UserProfileState(
    val name: String = "",
    val photo: Bitmap? = null,
    val learnedWordsCount: Int = 0,
    val levelProgress: Int = 0,
    val lessonProgress: Int = 0,
    val weekPlayingHours: Int = 0,
    val globalPlayingHours: Int = 0
)