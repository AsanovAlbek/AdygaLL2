package com.example.adygall2.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.adygall2.data.room.consts.RoomConst.EFFECT
import com.example.adygall2.data.room.consts.RoomConst.ID
import com.example.adygall2.data.room.consts.RoomConst.NAME
import com.example.adygall2.data.room.consts.RoomConst.SOUND_EFFECT_TABLE_NAME

/**
 * Entity класс для хранения данных из таблицы 'sound_effect'
 * @param id - id эффекта
 * @param name - название эффекта
 * @param effect - звуковой эффект в массиве байтов
 */
@Entity(tableName = SOUND_EFFECT_TABLE_NAME)
data class SoundEffectEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    val id : Int = 0,

    @ColumnInfo(name = NAME)
    val name : String,

    @ColumnInfo(name = EFFECT)
    val effect : ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SoundEffectEntity

        if (id != other.id) return false
        if (name != other.name) return false
        if (!effect.contentEquals(other.effect)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + effect.contentHashCode()
        return result
    }
}
