package com.example.adygall2.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.adygall2.data.room.consts.RoomConst.SOUNDS_TABLE_NAME
import com.example.adygall2.data.room.entities.SoundEntity

@Dao
abstract class SoundsDao {

    @Query("SELECT * FROM $SOUNDS_TABLE_NAME WHERE task_id = :taskId")
    abstract fun getSoundsByTaskId(taskId : Int) : List<SoundEntity>

    @Query("SELECT * FROM $SOUNDS_TABLE_NAME")
    abstract fun getAllSounds() : List<SoundEntity>

    @Query("SELECT * FROM $SOUNDS_TABLE_NAME WHERE task_id = :taskId")
    abstract fun getSoundById(taskId: Int) : SoundEntity
}