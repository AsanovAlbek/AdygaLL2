package com.example.adygall2.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.adygall2.data.room.consts.RoomConst.SOUNDS_TABLE_NAME
import com.example.adygall2.data.room.entities.SoundEntity

/**
 * DAO для запросов, связанных с озвучкой [SoundEntity]
 */
@Dao
abstract class SoundsDao {
    /** Получение всех озвучек */
    @Query("SELECT * FROM $SOUNDS_TABLE_NAME")
    abstract fun getAllSounds() : List<SoundEntity>

    /** Получение озвучки по id */
    @Query("SELECT * FROM $SOUNDS_TABLE_NAME WHERE id = :soundId")
    abstract fun getSoundById(soundId : Int) : SoundEntity
}