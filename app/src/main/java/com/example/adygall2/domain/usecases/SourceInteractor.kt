package com.example.adygall2.domain.usecases

import com.example.adygall2.domain.model.Answer
import com.example.adygall2.domain.repository.Repository

/** Интерактор для взаимодействия с Source моделями (озвучки, картинки, звуковые эффекты)*/
class SourceInteractor(private val repository: Repository) {
    /** Получение всех картинок */
    suspend fun allPictureSources() = repository.getAllPictures()
    /** Чистка кэша Glide */
    suspend fun clearGlideCaches() = repository.clearPicturesInCache()
    /** Получение озвучки по [sourceId] */
    suspend fun soundSourceById(sourceId : Int) = repository.getSourceSoundById(sourceId)
    /** Получение звукового эффекта для правильного ответа */
    suspend fun rightAnswerSource() = repository.rightAnswerSource()
    /** Получение звукового эффекта для неправильного ответа */
    suspend fun wrongAnswerSource() = repository.wrongAnswerSource()
    /** Все звуковые эффекты */
    suspend fun allSoundSources() = repository.getAllSourceSounds()
    /** Картинка по [pictureSourceId] */
    suspend fun pictureSourceById(pictureSourceId : Int) = repository.getPictureById(pictureSourceId)
    /** Картинки, соответствующие списку ответов [answers] */
    suspend fun picturesByAnswers(answers : List<Answer>) = repository.getPictureSourcesByAnswers(answers)
    /** Озвучки, соответствующие списку ответов [answers] */
    suspend fun soundsByAnswers(answers: List<Answer>) = repository.getSoundSourcesByAnswers(answers)
}