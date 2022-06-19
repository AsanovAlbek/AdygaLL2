package com.example.adygall2.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.adygall2.data.room.consts.RoomConst.ID
import com.example.adygall2.data.room.consts.RoomConst.PASSWORD
import com.example.adygall2.data.room.consts.RoomConst.ROLE
import com.example.adygall2.data.room.consts.RoomConst.USERS_TABLE_NAME
import com.example.adygall2.data.room.consts.RoomConst.USER_NAME

@Entity(tableName = USERS_TABLE_NAME)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name =  ID)
    val id : Int = 0,

    @ColumnInfo(name = USER_NAME)
    val userName : String,

    @ColumnInfo(name =  PASSWORD)
    val password : String,

    @ColumnInfo(name = ROLE)
    val role : String?
)
