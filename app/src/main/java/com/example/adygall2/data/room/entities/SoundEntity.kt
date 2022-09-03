package com.example.adygall2.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.adygall2.data.room.consts.RoomConst.ID
import com.example.adygall2.data.room.consts.RoomConst.NAME
import com.example.adygall2.data.room.consts.RoomConst.SOUND
import com.example.adygall2.data.room.consts.RoomConst.SOUNDS_TABLE_NAME

/**
 * Entity класс для хранения данных из таблицы 'sounds'
 * @param id - id эффекта
 * @param name - название эффекта
 * @param audioByteArray - звуковой эффект в массиве байтов
 */
@Entity(tableName = SOUNDS_TABLE_NAME)
data class SoundEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    val id : Int,

    @ColumnInfo(name = NAME)
    val name : String,

    @ColumnInfo(name = SOUND)
    val audioByteArray : ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SoundEntity

        if (id != other.id) return false
        if (name != other.name) return false
        if (!audioByteArray.contentEquals(other.audioByteArray)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + audioByteArray.contentHashCode()
        return result
    }
}