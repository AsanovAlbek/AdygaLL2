package com.example.adygall2.domain.usecases

import com.example.adygall2.data.db_models.Answer
import com.example.adygall2.domain.repository.Repository

class SoundUseCase(
    private val repository: Repository
) {
    fun getSoundById(soundId : Int) = repository.getSoundById(soundId)

    fun getSoundsByAnswers(answers : List<Answer>) = repository.getSoundsByAnswers(answers)
}