package com.example.adygall2.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.adygall2.data.room.consts.RoomConst.ID
import com.example.adygall2.data.room.consts.RoomConst.NAME
import com.example.adygall2.data.room.consts.RoomConst.PIC
import com.example.adygall2.data.room.consts.RoomConst.PICTURES_TABLE_NAME

@Entity(tableName = PICTURES_TABLE_NAME)
data class PictureEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    val id : Int = 0,

    @ColumnInfo(name = NAME)
    val name : String,

    @ColumnInfo(name = PIC)
    val picture : ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PictureEntity

        if (id != other.id) return false
        if (name != other.name) return false
        if (!picture.contentEquals(other.picture)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + picture.contentHashCode()
        return result
    }
}
