package com.example.adygall2.domain.usecases

import com.example.adygall2.domain.repository.Repository

class AnswerUseCase(
    private val repository: Repository
) {

    fun getAnswersByTaskId(taskId : Int) = repository.getAnswersByTaskId(taskId)
    fun getAllAnswers() = repository.getAllAnswers()
}