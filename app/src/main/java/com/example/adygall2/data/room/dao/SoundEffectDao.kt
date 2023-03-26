package com.example.adygall2.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.adygall2.data.room.consts.RoomConst.SOUND_EFFECT_TABLE_NAME
import com.example.adygall2.data.room.entities.SoundEffectEntity

/**
 * DAO для запросов, связанных с звуковыми эффектами [SoundEffectEntity]
 */
@Dao
abstract class SoundEffectDao {
    /** Получение звукового эффекта для правильного ответа */
    @Query("SELECT * FROM $SOUND_EFFECT_TABLE_NAME WHERE name = 'ok'")
    abstract fun rightAnswerSoundEffect() : SoundEffectEntity

    /** Получение звукового эффекта для неправильного ответа */
    @Query("SELECT * FROM $SOUND_EFFECT_TABLE_NAME WHERE name = 'error'")
    abstract fun wrongAnswerSoundEffect() : SoundEffectEntity
}