package com.example.adygall2.domain.usecases

import com.example.adygall2.domain.repository.Repository

/**
 * Класс для реализации логики, связанной с звуковыми эффектами
 */
class SoundEffectUseCase (
    private val repository: Repository
        ) {
    fun rightAnswerEffect() = repository.rightAnswerSoundEffect()

    fun wrongAnswerEffect() = repository.wrongAnswerSoundEffect()
}