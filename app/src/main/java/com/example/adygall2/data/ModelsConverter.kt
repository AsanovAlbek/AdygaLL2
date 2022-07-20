package com.example.adygall2.data

import com.example.adygall2.data.db_models.Answer
import com.example.adygall2.data.db_models.Order
import com.example.adygall2.data.db_models.Picture
import com.example.adygall2.data.db_models.Sound
import com.example.adygall2.data.db_models.Task
import com.example.adygall2.data.room.entities.AnswerEntity
import com.example.adygall2.data.room.entities.OrderEntity
import com.example.adygall2.data.room.entities.PictureEntity
import com.example.adygall2.data.room.entities.SoundEntity
import com.example.adygall2.data.room.entities.TaskEntity


fun AnswerEntity.toAnswer() = Answer(id, taskId, answer, isCorrectAnswer.lowercase(), pictureId)

fun OrderEntity.toOrder() = Order(id, taskNum)

fun PictureEntity.toPicture() = Picture(id, name, picture)

fun TaskEntity.toTask() = Task(id, taskType, task, soundId)

fun SoundEntity.toSound() = Sound(id, name, audioByteArray)


