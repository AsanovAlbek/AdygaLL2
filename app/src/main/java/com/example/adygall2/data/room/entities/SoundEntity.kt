package com.example.adygall2.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.adygall2.data.room.consts.RoomConst.FILE_NAME
import com.example.adygall2.data.room.consts.RoomConst.ID
import com.example.adygall2.data.room.consts.RoomConst.SOUNDS_TABLE_NAME
import com.example.adygall2.data.room.consts.RoomConst.TASK_ID

@Entity(tableName = SOUNDS_TABLE_NAME)
data class SoundEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    val id : Int,

    @ColumnInfo(name = TASK_ID)
    val taskId : Int,

    @ColumnInfo(name = FILE_NAME)
    val filePath : String
)