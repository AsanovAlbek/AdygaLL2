package com.example.adygall2.domain.model

import com.example.adygall2.data.room.userbase.ProgressItem

data class User(
    val id: Int = 1,
    val name: String = "",
    val hp: Int = 100,
    val coins: Int = 0,
    val isUserSignUp: Boolean = false,
    val lastOnlineTimeInMillis: Long = 0L,
    val learnedWords: MutableSet<String> = mutableSetOf(),
    val learningProgressSet: MutableSet<ProgressItem> = mutableSetOf(),
    val globalPlayingTimeInMillis: Long = 0L
)
