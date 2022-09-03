package com.example.adygall2.domain.usecases

import com.example.adygall2.data.db_models.Answer
import com.example.adygall2.domain.repository.Repository

/**
 * Класс для реализации логики, связанной с картинками
 */
class PictureUseCase(
    private val repository: Repository
) {
    fun getPicturesByAnswers(answers : List<Answer>) = repository.getPicturesByAnswers(answers)

    fun clearCaches() = repository.clearPicturesInCache()
}