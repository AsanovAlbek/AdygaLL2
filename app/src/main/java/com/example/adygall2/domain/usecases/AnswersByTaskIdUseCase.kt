package com.example.adygall2.domain.usecases

import com.example.adygall2.domain.repository.Repository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Класс для реализации логики, связанной с ответами
 */
class AnswersByTaskIdUseCase(
    private val repository: Repository,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(taskId: Int) = withContext(ioDispatcher) {
        return@withContext repository.getAnswersByTaskId(taskId)
    }
}