package com.example.adygall2.domain.usecases

import com.example.adygall2.domain.model.Answer
import com.example.adygall2.domain.model.ComplexAnswer
import com.example.adygall2.domain.repository.Repository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetComplexAnswerUseCase(
    private val repository: Repository,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(answers: MutableList<Answer>) : List<ComplexAnswer> =
        withContext(ioDispatcher) {
            return@withContext answers.map { repository.answerToComplexAnswer(it) }
        }

}