package com.example.adygall2.data.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.adygall2.data.room.consts.RoomConst.PATH_TO_DATABASE_2
import com.example.adygall2.data.room.consts.RoomConst.PATH_TO_DATABASE_3
import com.example.adygall2.data.room.dao.*
import com.example.adygall2.data.room.entities.*

@Database(
    entities = [
        AnswerEntity::class,
        OrderEntity::class,
        PictureEntity::class,
        TaskEntity::class,
        UserEntity::class,
        SoundEntity::class
    ], version = 1, exportSchema = false
)
abstract class GameBase : RoomDatabase() {

    companion object {
        fun buildDatabase(context : Context) =
            Room.databaseBuilder(context, GameBase::class.java, "game_database").
                    fallbackToDestructiveMigration().createFromAsset(PATH_TO_DATABASE_3).build()
    }

    abstract fun getAnswerDao() : AnswerDao
    abstract fun getOrderDao() : OrderDao
    abstract fun getPictureDao() : PictureDao
    abstract fun getTaskDao() : TaskDao
    abstract fun getUserDao() : UserDao
    abstract fun getSoundsDao() : SoundsDao
}