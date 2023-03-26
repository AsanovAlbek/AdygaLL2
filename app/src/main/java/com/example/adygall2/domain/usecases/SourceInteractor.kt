package com.example.adygall2.domain.usecases

import com.example.adygall2.domain.repository.Repository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/** Интерактор для взаимодействия с Source моделями (озвучки, картинки, звуковые эффекты)*/
class SourceInteractor(
    private val repository: Repository,
    private val ioDispatcher: CoroutineDispatcher
    ) {
    /** Чистка кэша Glide */
    suspend fun clearGlideCaches() = withContext(ioDispatcher) {
        return@withContext repository.clearPicturesInCache()
    }
    /** Получение озвучки по [sourceId] */
    suspend fun soundSourceById(sourceId : Int) = withContext(ioDispatcher) {
        return@withContext repository.getSourceSoundById(sourceId)
    }
    /** Получение звукового эффекта для правильного ответа */
    suspend fun rightAnswerSource() = withContext(ioDispatcher) {
        return@withContext repository.rightAnswerSource()
    }
    /** Получение звукового эффекта для неправильного ответа */
    suspend fun wrongAnswerSource() = withContext(ioDispatcher) {
        return@withContext repository.wrongAnswerSource()
    }
}