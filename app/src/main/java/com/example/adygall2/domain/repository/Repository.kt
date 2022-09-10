package com.example.adygall2.domain.repository

import com.example.adygall2.domain.model.Answer
import com.example.adygall2.domain.model.ComplexAnswer
import com.example.adygall2.domain.model.Order
import com.example.adygall2.domain.model.Source
import com.example.adygall2.domain.model.Task

/**
 * Репозиторий для работы с бд
 */
interface Repository {

    // Orders

    /** Получение Order по id */
    suspend fun getOrderById(orderId : Int) : Order

    /** Получение всех Order */
    suspend fun getAllOrders() : List<Order>

    // Tasks

    /** Получение [Task] по [TaskType] */
    suspend fun getTasksByType(taskType : Int) : List<Task>

    /** Получение [Task] по id */
    suspend fun getTaskById(taskId : Int) : Task

    /** Получение списка [Task] из списка [Order] */
    suspend fun getTasksFromOrder(orders : List<Order>) : List<Task>

    // Source

    /** Получение всех [Source] картинок */
    suspend fun getAllPictures() : List<Source>

    suspend fun getPictureById(pictureSourceId : Int) : Source

    /** Получение всех [Source] по соответствующим [Answer] */
    suspend fun getPictureSourcesByAnswers(answers : List<Answer>) : List<Source>

    suspend fun getSoundSourcesByAnswers(answers: List<Answer>) : List<Source>

    /** Очистка кэша Glide */
    suspend fun clearPicturesInCache()

    /** Получить [Source] озвучку по id */
    suspend fun getSourceSoundById(sourceId : Int) : Source

    /** Получить все озвучки */
    suspend fun getAllSourceSounds() : List<Source>

    /** Получить аудиофайл для правильного ответа */
    suspend fun rightAnswerSource() : Source

    /** Получить аудиофайл для неправильного ответа */
    suspend fun wrongAnswerSource() : Source

    // Answers

    /** Получение [Answer] по id задания */
    suspend fun getAnswersByTaskId(taskId : Int) : MutableList<Answer>

    /** Получить все [Answer] */
    suspend fun getAllAnswers() : List<Answer>

    // ComplexAnswer
    /** Получение модели ответа с озвучкой и картинкой */
    suspend fun answerToComplexAnswer(answer : Answer) : ComplexAnswer

}