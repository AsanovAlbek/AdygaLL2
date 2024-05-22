package com.example.adygall2.data.room.gamebase.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.adygall2.data.room.consts.RoomConst

data class LevelsEntity(
    @ColumnInfo(name = RoomConst.LEVEL)
    val level : Int = 0,
    @ColumnInfo(name = RoomConst.LESSON)
    val lesson : Int = 0,
)
