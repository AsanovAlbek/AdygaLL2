package com.example.adygall2.domain.usecases

import com.example.adygall2.domain.repository.Repository

/** Интерактор для взаимодействия с Source моделями (озвучки, картинки, звуковые эффекты)*/
class SourceInteractor(private val repository: Repository) {
    /** Чистка кэша Glide */
    suspend fun clearGlideCaches() = repository.clearPicturesInCache()
    /** Получение озвучки по [sourceId] */
    suspend fun soundSourceById(sourceId : Int) = repository.getSourceSoundById(sourceId)
    /** Получение звукового эффекта для правильного ответа */
    suspend fun rightAnswerSource() = repository.rightAnswerSource()
    /** Получение звукового эффекта для неправильного ответа */
    suspend fun wrongAnswerSource() = repository.wrongAnswerSource()
}