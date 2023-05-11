package com.example.adygall2.domain.repository

import com.example.adygall2.domain.model.Answer
import com.example.adygall2.domain.model.ComplexAnswer
import com.example.adygall2.domain.model.Order
import com.example.adygall2.domain.model.Source
import com.example.adygall2.domain.model.Task
import com.example.adygall2.domain.model.User

/**
 * Репозиторий для работы с бд
 */
interface Repository {
    // User
    suspend fun getUser(): User

    suspend fun updateUser(user: User)

    suspend fun isUserExist(): Boolean

    // Orders

    /** Получение всех Order */
    suspend fun getAllOrders() : List<Order>

    // Tasks

    /** Получение [Task] по id */
    suspend fun getTaskById(taskId : Int) : Task

    /** Получение списка [Task] из списка [Order] */
    suspend fun getTasksFromOrder(orders : List<Order>) : List<Task>

    // Source

    suspend fun getPictureById(pictureSourceId : Int) : Source

    /** Получение всех [Source] по соответствующим [Answer] */
    suspend fun getPictureSourcesByAnswers(answers : List<Answer>) : List<Source>

    /** Очистка кэша Glide */
    suspend fun clearPicturesInCache()

    /** Получить [Source] озвучку по id */
    suspend fun getSourceSoundById(sourceId : Int) : Source

    /** Получить аудиофайл для правильного ответа */
    suspend fun rightAnswerSource() : Source

    /** Получить аудиофайл для неправильного ответа */
    suspend fun wrongAnswerSource() : Source

    // Answers

    /** Получение [Answer] по id задания */
    suspend fun getAnswersByTaskId(taskId : Int) : MutableList<Answer>

    // ComplexAnswer
    /** Получение модели ответа с озвучкой и картинкой */
    suspend fun answerToComplexAnswer(answer : Answer) : ComplexAnswer

}