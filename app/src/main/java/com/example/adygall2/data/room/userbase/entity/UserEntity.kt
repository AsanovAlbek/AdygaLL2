package com.example.adygall2.data.room.userbase.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.adygall2.data.room.consts.RoomConst.COINS
import com.example.adygall2.data.room.consts.RoomConst.GLOBAL_PLAYED_TIME
import com.example.adygall2.data.room.consts.RoomConst.HP
import com.example.adygall2.data.room.consts.RoomConst.ID
import com.example.adygall2.data.room.consts.RoomConst.IS_USER_SIGN_UP
import com.example.adygall2.data.room.consts.RoomConst.LAST_ONLINE
import com.example.adygall2.data.room.consts.RoomConst.LEARNED_WORDS
import com.example.adygall2.data.room.consts.RoomConst.LEARNING_PROGRESS
import com.example.adygall2.data.room.consts.RoomConst.NAME
import com.example.adygall2.data.room.consts.RoomConst.USER_TABLE
import com.example.adygall2.data.room.userbase.ProgressItem

@Entity(tableName = USER_TABLE)
data class UserEntity(
    @ColumnInfo(name = ID)
    @PrimaryKey(autoGenerate = false)
    val id: Int = 1,
    @ColumnInfo(name = NAME)
    val name: String = "",
    @ColumnInfo(name = HP)
    val hp: Int = 100,
    @ColumnInfo(name = COINS)
    val coins: Int = 0,
    @ColumnInfo(name = IS_USER_SIGN_UP)
    val isUserSignUp: Boolean = false,
    @ColumnInfo(name = LAST_ONLINE)
    val lastOnlineTimeInMillis: Long = 0L,
    @ColumnInfo(name = LEARNED_WORDS)
    val learnedWords: MutableSet<String> = mutableSetOf(),
    @ColumnInfo(name = LEARNING_PROGRESS)
    val learningProgressSet: MutableSet<ProgressItem> = mutableSetOf(),
    @ColumnInfo(name = GLOBAL_PLAYED_TIME)
    val globalPlayingTimeInMillis: Long = 0L
)
