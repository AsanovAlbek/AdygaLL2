package com.example.adygall2.data.room.userbase.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.adygall2.data.room.consts.RoomConst.ID
import com.example.adygall2.data.room.consts.RoomConst.USER_TABLE
import com.example.adygall2.data.room.userbase.entity.UserEntity

@Dao
abstract class UserDao {
    @Query("Select * from $USER_TABLE where id = 1")
    abstract fun getUser(): UserEntity

    @Upsert
    abstract fun upsertUser(user: UserEntity)

    @Query("Select exists (Select $ID from $USER_TABLE where id = 1)")
    abstract fun isUserExist(): Boolean
}