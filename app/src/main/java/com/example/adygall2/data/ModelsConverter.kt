package com.example.adygall2.data

import com.example.adygall2.data.db_models.*
import com.example.adygall2.data.room.entities.*

fun AnswerEntity.toAnswer() = Answer(
    id, taskId, answer, picture, isCorrectAnswer.lowercase().toBoolean(), authorOfChanges, dateOfChange
)

fun OrderEntity.toOrder() = Order(id, taskNum)

fun PictureEntity.toPicture() = Picture(id, name, picture)

fun TaskEntity.toTask() = Task(id, taskType, task, authorOfChanges, dateOfChange)

fun UserEntity.toUser() = User(id, userName, password, role)

fun SoundEntity.toSound() = Sound(id, taskId, filePath)


// Обычно нужно для добавления в бд, но пусть будет на всякий случай
fun Picture.toPictureEntity() = PictureEntity(id, name, picture)

fun Order.toOrderEntity() = OrderEntity(id, taskNum)

fun Task.toTaskEntity() = TaskEntity(id, taskType, task, authorOfChanges, dateOfChange)

fun Answer.toAnswerEntity() = AnswerEntity(
    id, taskId, answer, pictureId, isCorrectAnswer.toString(), authorOfChanges, dateOfChange
)

fun User.toUserEntity() = UserEntity(id, userName, password, role)


