package com.example.adygall2.domain.usecases

import com.example.adygall2.domain.repository.Repository

/**
 * Класс для реализации логики, связанной с ответами
 */
class AnswerUseCase(
    private val repository: Repository
) {
    fun getAnswersByTaskId(taskId : Int) = repository.getAnswersByTaskId(taskId)
}