package com.example.adygall2.data.room.userbase.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.adygall2.data.room.userbase.converter.UserLearnedWordsConverter
import com.example.adygall2.data.room.userbase.converter.UserLearningProgressConverter
import com.example.adygall2.data.room.userbase.dao.UserDao
import com.example.adygall2.data.room.userbase.entity.UserEntity

@TypeConverters(UserLearnedWordsConverter::class, UserLearningProgressConverter::class)
@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class UserDataBase : RoomDatabase() {
    companion object {
        fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, UserDataBase::class.java, "user_database")
                .fallbackToDestructiveMigration().build()
    }

    abstract fun getUserDao(): UserDao
}