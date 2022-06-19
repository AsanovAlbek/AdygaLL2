package com.example.adygall2.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.adygall2.data.room.consts.RoomConst.USERS_TABLE_NAME
import com.example.adygall2.data.room.entities.UserEntity

@Dao
abstract class UserDao {
    @Query("SELECT * FROM $USERS_TABLE_NAME")
    abstract fun getAllUsers() : List<UserEntity>

    @Query("SELECT * FROM $USERS_TABLE_NAME WHERE id = :userId")
    abstract fun getUserById(userId : Int) : UserEntity
}