package com.example.adygall2.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.adygall2.data.room.consts.RoomConst.ID
import com.example.adygall2.data.room.consts.RoomConst.SOUND
import com.example.adygall2.data.room.consts.RoomConst.TASK
import com.example.adygall2.data.room.consts.RoomConst.TASKS_TABLE_NAME
import com.example.adygall2.data.room.consts.RoomConst.TASK_TYPE

@Entity(tableName = TASKS_TABLE_NAME)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    val id : Int = 0,

    @ColumnInfo(name = TASK_TYPE)
    val taskType : Int,

    @ColumnInfo(name = TASK)
    val task : String,

    @ColumnInfo(name = SOUND)
    val soundId : Int
)
