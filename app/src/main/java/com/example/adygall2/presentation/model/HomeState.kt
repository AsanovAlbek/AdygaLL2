package com.example.adygall2.presentation.model

import com.example.adygall2.domain.model.LevelAndLesson
import com.example.adygall2.domain.model.Task

data class HomeState(
    val loading: Boolean = true,
    val levelsAndLessons: List<LevelAndLesson> = emptyList(),
    val tasks: List<Task> = emptyList()
)