package com.example.adygall2.data.repository

import android.content.Context
import com.bumptech.glide.Glide
import com.example.adygall2.data.room.dao.AnswerDao
import com.example.adygall2.data.room.dao.OrderDao
import com.example.adygall2.data.room.dao.PictureDao
import com.example.adygall2.data.room.dao.SoundEffectDao
import com.example.adygall2.data.room.dao.SoundsDao
import com.example.adygall2.data.room.dao.TaskDao
import com.example.adygall2.data.toAnswer
import com.example.adygall2.data.toOrder
import com.example.adygall2.data.toSource
import com.example.adygall2.data.toTask
import com.example.adygall2.domain.model.Answer
import com.example.adygall2.domain.model.ComplexAnswer
import com.example.adygall2.domain.model.Order
import com.example.adygall2.domain.model.Source
import com.example.adygall2.domain.model.Task
import com.example.adygall2.domain.repository.Repository

/**
 * Репозиторий для взаимодействия с элементами из бд
 */
class RepositoryImpl(
    private val context : Context,
    private val answerDao: AnswerDao,
    private val orderDao: OrderDao,
    private val pictureDao: PictureDao,
    private val taskDao: TaskDao,
    private val soundsDao: SoundsDao,
    private val soundEffectDao: SoundEffectDao
) : Repository {

    // Answer
    override suspend fun getAnswersByTaskId(taskId: Int): MutableList<Answer> =
        answerDao.getTaskAnswers(taskId).map { it.toAnswer() }.toMutableList()

    override suspend fun getAllAnswers(): List<Answer> =
        answerDao.getAllAnswers().map { it.toAnswer() }

    // Order
    override suspend fun getOrderById(orderId: Int): Order =
        orderDao.getOrder(orderId).toOrder()

    override suspend fun getAllOrders(): List<Order> =
        orderDao.getAllOrders().map { it.toOrder() }


    //Source
    override suspend fun getPictureSourcesByAnswers(answers: List<Answer>): List<Source> =
        answers.map { pictureDao.getPicture(it.pictureId).toSource() }

    override suspend fun getAllPictures(): List<Source> =
        pictureDao.getAllPictures().map { it.toSource() }

    override suspend fun getPictureById(pictureSourceId: Int): Source =
        pictureDao.getPicture(pictureSourceId).toSource()

    override suspend fun clearPicturesInCache() {
        Glide.get(context).clearDiskCache()
    }

    override suspend fun getSoundSourcesByAnswers(answers: List<Answer>): List<Source> =
        answers.map { soundsDao.getSoundById(it.soundId).toSource() }

    override suspend fun getSourceSoundById(sourceId: Int) =
        soundsDao.getSoundById(sourceId).toSource()

    override suspend fun getAllSourceSounds(): List<Source> =
        soundsDao.getAllSounds().map { it.toSource() }

    override suspend fun rightAnswerSource(): Source =
        soundEffectDao.rightAnswerSoundEffect().toSource()

    override suspend fun wrongAnswerSource(): Source =
        soundEffectDao.wrongAnswerSoundEffect().toSource()

    //Task
    override suspend fun getTasksByType(taskType : Int): List<Task> =
        taskDao.getTaskByType(taskType).map { it.toTask() }

    override suspend fun getTaskById(taskId : Int): Task =
        taskDao.getTaskById(taskId).toTask()

    override suspend fun getTasksFromOrder(orders: List<Order>): List<Task> =
        orders.map { getTaskById(it.taskNum) }

    override suspend fun answerToComplexAnswer(answer: Answer): ComplexAnswer {
        val picture = getPictureById(answer.pictureId)
        val sound = getSourceSoundById(answer.soundId)
        return ComplexAnswer(answer, sound, picture)
    }
}