package com.example.adygall2.data.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.adygall2.data.*
import com.example.adygall2.data.db_models.*
import com.example.adygall2.data.room.dao.*
import com.example.adygall2.domain.repository.Repository

class RepositoryImpl(
    private val answerDao: AnswerDao,
    private val orderDao: OrderDao,
    private val pictureDao: PictureDao,
    private val taskDao: TaskDao,
    private val userDao: UserDao,
    private val soundsDao: SoundsDao
) : Repository {

    // Answer
    override fun getAnswersByTaskId(taskId: Int): List<Answer> =
        answerDao.getTaskAnswers(taskId).map { it.toAnswer() }

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

    override fun getTasksFromOrder(orders: List<Order>): List<Task> {
        val tasksList = mutableListOf<Task>()
        orders.forEach {
            tasksList.add(getTaskById(it.taskNum))
        }

        return tasksList.toList()
    }

    //User
    override fun getUserById(userId: Int): User = userDao.getUserById(userId).toUser()

    override fun getAllUsers(): List<User> =
        userDao.getAllUsers().map { it.toUser() }

    // Sound

    override fun getSoundsByTaskId(taskId: Int): List<Sound> =
        soundsDao.getSoundsByTaskId(taskId).map {
            it.toSound()
        }

    override fun getAllSounds(): List<Sound> =
        soundsDao.getAllSounds().map { it.toSound() }

    override fun getSoundByTaskId(taskId: Int): Sound =
        soundsDao.getSoundById(taskId).toSound()
}