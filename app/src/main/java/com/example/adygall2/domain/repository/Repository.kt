package com.example.adygall2.domain.repository

import com.example.adygall2.data.db_models.Answer
import com.example.adygall2.data.db_models.Order
import com.example.adygall2.data.db_models.Picture
import com.example.adygall2.data.db_models.Sound
import com.example.adygall2.data.db_models.SoundEffect
import com.example.adygall2.data.db_models.Task
import com.example.adygall2.data.room.dao.SoundEffectDao

interface Repository {

    // Orders

    fun getOrderById(orderId : Int) : Order

    fun getAllOrders() : List<Order>

    // Tasks

    fun getTasksByType(taskType : Int) : List<Task>

    fun getTaskById(taskId : Int) : Task

    fun getTasksFromOrder(orders : List<Order>) : List<Task>

    // Pictures

    fun getAllPictures() : List<Picture>

    fun getPicturesByAnswers(answers : List<Answer>) : List<Picture>

    // Answers

    fun getAnswersByTaskId(taskId : Int) : MutableList<Answer>

    fun getAllAnswers() : List<Answer>

    // Sounds
    fun getAllSounds() : List<Sound>

    fun getSoundById(soundId : Int) : Sound

    fun getSoundsByAnswers(answers : List<Answer>) : List<Sound>

    // SoundEffects
    fun rightAnswerSoundEffect() : SoundEffect

    fun wrongAnswerSoundEffect() : SoundEffect
}