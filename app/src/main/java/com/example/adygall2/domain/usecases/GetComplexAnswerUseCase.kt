package com.example.adygall2.domain.usecases

import com.example.adygall2.domain.model.Answer
import com.example.adygall2.domain.model.ComplexAnswer
import com.example.adygall2.domain.repository.Repository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetComplexAnswerUseCase : KoinComponent {
    private val repository : Repository by inject()
    suspend operator fun invoke(answers: MutableList<Answer>) : List<ComplexAnswer> =
        answers.map { repository.answerToComplexAnswer(it) }
}