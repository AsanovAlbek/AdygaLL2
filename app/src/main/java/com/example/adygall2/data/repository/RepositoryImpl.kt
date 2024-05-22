package com.example.adygall2.data.repository

import android.content.Context
import android.util.Log
import com.bumptech.glide.Glide
import com.example.adygall2.data.room.gamebase.dao.AnswerDao
import com.example.adygall2.data.room.gamebase.dao.OrderDao
import com.example.adygall2.data.room.gamebase.dao.PictureDao
import com.example.adygall2.data.room.gamebase.dao.SoundEffectDao
import com.example.adygall2.data.room.gamebase.dao.SoundsDao
import com.example.adygall2.data.room.gamebase.dao.TaskDao
import com.example.adygall2.data.room.userbase.dao.UserDao
import com.example.adygall2.data.toAnswer
import com.example.adygall2.data.toDomain
import com.example.adygall2.data.toEntity
import com.example.adygall2.data.toOrder
import com.example.adygall2.data.toSource
import com.example.adygall2.data.toTask
import com.example.adygall2.data.toUser
import com.example.adygall2.domain.model.Answer
import com.example.adygall2.domain.model.ComplexAnswer
import com.example.adygall2.domain.model.LevelAndLesson
import com.example.adygall2.domain.model.Order
import com.example.adygall2.domain.model.Source
import com.example.adygall2.domain.model.Task
import com.example.adygall2.domain.model.User
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
    private val soundEffectDao: SoundEffectDao,
    private val userDao: UserDao
) : Repository {

    // Answer
    override suspend fun getAnswersByTaskId(taskId: Int): MutableList<Answer> =
        answerDao.getTaskAnswers(taskId).map { it.toAnswer() }.toMutableList()

    // User
    override suspend fun getUser(): User {
        val user = userDao.getUser().toUser()
        Log.i("repository", "getted user is $user")
        return user
    }

    override suspend fun updateUser(user: User) {
        Log.i("repository", "upserted user is $user")
        userDao.upsertUser(user.toEntity())
    }

    override suspend fun isUserExist(): Boolean = userDao.isUserExist()

    // Order

    override suspend fun getAllOrders(): List<Order> =
        orderDao.getAllOrders().map { it.toOrder() }


    //Source
    override suspend fun getPictureSourcesByAnswers(answers: List<Answer>): List<Source> =
        answers.map { pictureDao.getPicture(it.pictureId).toSource() }

    override suspend fun getPictureById(pictureSourceId: Int): Source =
        pictureDao.getPicture(pictureSourceId).toSource()

    override suspend fun clearPicturesInCache() {
        Glide.get(context).clearDiskCache()
    }

    override suspend fun getSourceSoundById(sourceId: Int): Source =
        soundsDao.getSoundById(sourceId).toSource()

    override suspend fun rightAnswerSource(): Source =
        soundEffectDao.rightAnswerSoundEffect().toSource()

    override suspend fun wrongAnswerSource(): Source =
        soundEffectDao.wrongAnswerSoundEffect().toSource()

    //Task

    override suspend fun getTaskById(taskId : Int): Task =
        taskDao.getTaskById(taskId).toTask()

    override suspend fun getTasksFromOrder(orders: List<Order>): List<Task> =
        orders.map { getTaskById(it.taskNum) }

    override suspend fun answerToComplexAnswer(answer: Answer): ComplexAnswer {
        val picture = getPictureById(answer.pictureId)
        val sound = getSourceSoundById(answer.soundId)
        return ComplexAnswer(answer, sound, picture)
    }

    override suspend fun tasksByLesson(level: Int, lesson: Int): List<Task> {
        return taskDao.getTasksByLesson(level, lesson).map { it.toTask() }
    }

    override suspend fun allLevelsAndLessons(): List<LevelAndLesson> {
        return taskDao.getAllLevelsAndLessons().map { it.toDomain() }
    }
}