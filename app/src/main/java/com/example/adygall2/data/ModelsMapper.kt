package com.example.adygall2.data

import com.example.adygall2.domain.model.Answer
import com.example.adygall2.domain.model.Order
import com.example.adygall2.domain.model.Task
import com.example.adygall2.data.room.entities.AnswerEntity
import com.example.adygall2.data.room.entities.OrderEntity
import com.example.adygall2.data.room.entities.PictureEntity
import com.example.adygall2.data.room.entities.SoundEffectEntity
import com.example.adygall2.data.room.entities.SoundEntity
import com.example.adygall2.data.room.entities.TaskEntity
import com.example.adygall2.domain.model.Source

/**
 * Модуль для маппинга Entity классов в модели
 */

fun AnswerEntity.toAnswer() =
    Answer(id, taskId, answer, isCorrectAnswer.lowercase(), pictureId, soundId)

fun OrderEntity.toOrder() = Order(id, taskNum)

fun PictureEntity.toSource() = Source(id, name, source = picture)

fun SoundEntity.toSource() = Source(id, name, source = audioByteArray)

fun SoundEffectEntity.toSource() = Source(id, name, source = effect)

fun TaskEntity.toTask() = Task(id, taskType, task, soundId, levelId, lessonId, exerciseId)
