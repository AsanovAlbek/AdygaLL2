package com.example.adygall2.domain.usecases

import com.example.adygall2.domain.repository.Repository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class TasksByLessonUseCase(
    private val repository: Repository,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(level: Int, lesson: Int) = withContext(ioDispatcher) {
        repository.tasksByLesson(level, lesson)
    }

    suspend fun allLessons() = withContext(ioDispatcher) {
        repository.allLevelsAndLessons()
    }
}