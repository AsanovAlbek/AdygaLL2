package com.example.adygall2.domain.usecases

import com.example.adygall2.domain.repository.Repository

class SoundUseCase(
    private val repository: Repository
) {

    fun getSoundsByTaskId(taskId : Int) = repository.getSoundsByTaskId(taskId)

    fun getAllSounds() = repository.getAllSounds()

    fun getSoundById(taskId : Int) = repository.getSoundByTaskId(taskId)

}