package com.example.adygall2.domain.usecases

import com.example.adygall2.domain.repository.Repository

class SoundUseCase(
    private val repository: Repository
) {

    //fun getSoundById(taskId : Int) = repository.getSoundByTaskId(taskId)

    fun getSoundById(soundId : Int) = repository.getSoundById(soundId)

}