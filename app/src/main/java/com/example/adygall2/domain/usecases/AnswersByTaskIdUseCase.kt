package com.example.adygall2.domain.usecases

import com.example.adygall2.domain.repository.Repository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Класс для реализации логики, связанной с ответами
 */
class AnswersByTaskIdUseCase : KoinComponent {
    private val repository : Repository by inject()
    suspend operator fun invoke(taskId: Int) = repository.getAnswersByTaskId(taskId)
}