package com.example.adygall2.domain.repository

import com.example.adygall2.data.db_models.*

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

    fun getAnswersByTaskId(taskId : Int) : List<Answer>

    fun getAllAnswers() : List<Answer>

    // Sounds

    //fun getSoundsByTaskId(taskId : Int) : List<Sound>

    fun getAllSounds() : List<Sound>

    //fun getSoundByTaskId(taskId : Int) : Sound

    fun getSoundById(soundId : Int) : Sound
}