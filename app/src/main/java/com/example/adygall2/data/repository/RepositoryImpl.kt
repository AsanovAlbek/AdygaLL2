package com.example.adygall2.data.repository

import com.example.adygall2.data.*
import com.example.adygall2.data.db_models.*
import com.example.adygall2.data.room.dao.*
import com.example.adygall2.domain.repository.Repository

class RepositoryImpl(
    private val answerDao: AnswerDao,
    private val orderDao: OrderDao,
    private val pictureDao: PictureDao,
    private val taskDao: TaskDao,
    private val soundsDao: SoundsDao,
    private val soundEffectDao: SoundEffectDao
) : Repository {

    // Answer
    override fun getAnswersByTaskId(taskId: Int): MutableList<Answer> =
        answerDao.getTaskAnswers(taskId).map { it.toAnswer() }.toMutableList()

    override fun getAllAnswers(): List<Answer> =
        answerDao.getAllAnswers().map { it.toAnswer() }

    // Order
    override fun getOrderById(orderId: Int): Order =
        orderDao.getOrder(orderId).toOrder()

    override fun getAllOrders(): List<Order> =
        orderDao.getAllOrders().map { it.toOrder() }


    //Picture
    override fun getPicturesByAnswers(answers: List<Answer>): List<Picture> =
        answers.map { pictureDao.getPicture(it.pictureId).toPicture() }

    override fun getAllPictures(): List<Picture> =
        pictureDao.getAllPictures().map { it.toPicture() }


    //Task
    override fun getTasksByType(taskType : Int): List<Task> =
        taskDao.getTaskByType(taskType).map { it.toTask() }

    override fun getTaskById(taskId : Int): Task =
        taskDao.getTaskById(taskId).toTask()

    override fun getTasksFromOrder(orders: List<Order>): List<Task> =
        orders.map { getTaskById(it.taskNum) }

    // Sound

    override fun getAllSounds(): List<Sound> =
        soundsDao.getAllSounds().map { it.toSound() }

    override fun getSoundById(soundId: Int) =
        soundsDao.getSoundById(soundId).toSound()

    override fun getSoundsByAnswers(answers: List<Answer>): List<Sound> =
        answers.map { getSoundById(it.soundId) }

    // SoundEffect
    override fun rightAnswerSoundEffect(): SoundEffect =
        soundEffectDao.rightAnswerSoundEffect().toSoundEffect()

    override fun wrongAnswerSoundEffect(): SoundEffect =
        soundEffectDao.wrongAnswerSoundEffect().toSoundEffect()
}