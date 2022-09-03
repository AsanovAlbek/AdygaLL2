package com.example.adygall2.domain.repository

import com.example.adygall2.data.db_models.Answer
import com.example.adygall2.data.db_models.Order
import com.example.adygall2.data.db_models.Picture
import com.example.adygall2.data.db_models.Sound
import com.example.adygall2.data.db_models.SoundEffect
import com.example.adygall2.data.db_models.Task

/**
 * Репозиторий для работы с бд
 */
interface Repository {

    // Orders

    /** Получение Order по id */
    fun getOrderById(orderId : Int) : Order

    /** Получение всех Order */
    fun getAllOrders() : List<Order>

    // Tasks

    /** Получение [Task] по [TaskType] */
    fun getTasksByType(taskType : Int) : List<Task>

    /** Получение [Task] по id */
    fun getTaskById(taskId : Int) : Task

    /** Получение списка [Task] из списка [Order] */
    fun getTasksFromOrder(orders : List<Order>) : List<Task>

    // Pictures

    /** Получение всех [Picture] */
    fun getAllPictures() : List<Picture>

    /** Получение всех [Picture] по соответствующим [Answer] */
    fun getPicturesByAnswers(answers : List<Answer>) : List<Picture>

    /** Очистка кэша Glide */
    fun clearPicturesInCache()

    // Answers

    /** Получение [Answer] по id задания */
    fun getAnswersByTaskId(taskId : Int) : MutableList<Answer>

    /** Получить все [Answer] */
    fun getAllAnswers() : List<Answer>

    // Sounds
    /** Получить все [Sound] */
    fun getAllSounds() : List<Sound>

    /** Получить [Sound] по id */
    fun getSoundById(soundId : Int) : Sound

    /** Получить [Sound] из списка [Answer] */
    fun getSoundsByAnswers(answers : List<Answer>) : List<Sound>

    // SoundEffects
    /** Получить аудиофайл для правильного ответа */
    fun rightAnswerSoundEffect() : SoundEffect

    /** Получить аудиофайл для неправильного ответа */
    fun wrongAnswerSoundEffect() : SoundEffect
}