package com.example.adygall2.data

import com.example.adygall2.data.room.gamebase.entities.AnswerEntity
import com.example.adygall2.data.room.gamebase.entities.OrderEntity
import com.example.adygall2.data.room.gamebase.entities.PictureEntity
import com.example.adygall2.data.room.gamebase.entities.SoundEffectEntity
import com.example.adygall2.data.room.gamebase.entities.SoundEntity
import com.example.adygall2.data.room.gamebase.entities.TaskEntity
import com.example.adygall2.data.room.userbase.entity.UserEntity
import com.example.adygall2.domain.model.Answer
import com.example.adygall2.domain.model.Order
import com.example.adygall2.domain.model.Source
import com.example.adygall2.domain.model.Task
import com.example.adygall2.domain.model.User

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

fun User.toEntity() = UserEntity(
    id = id,
    name = name,
    hp = hp,
    coins = coins,
    isUserSignUp = isUserSignUp,
    lastOnlineTimeInMillis = lastOnlineTimeInMillis,
    learnedWords = learnedWords,
    learningProgressSet = learningProgressSet,
    globalPlayingTimeInMillis = globalPlayingTimeInMillis
)

fun UserEntity.toUser() = User(
    id = id,
    name = name,
    hp = hp,
    coins = coins,
    isUserSignUp = isUserSignUp,
    lastOnlineTimeInMillis = lastOnlineTimeInMillis,
    learnedWords = learnedWords,
    learningProgressSet = learningProgressSet,
    globalPlayingTimeInMillis = globalPlayingTimeInMillis
)
