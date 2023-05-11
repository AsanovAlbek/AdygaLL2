package com.example.adygall2.data.room.gamebase.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.adygall2.data.room.consts.RoomConst.FULL_BASE
import com.example.adygall2.data.room.consts.RoomConst.SECOND_BASE
import com.example.adygall2.data.room.gamebase.dao.AnswerDao
import com.example.adygall2.data.room.gamebase.dao.OrderDao
import com.example.adygall2.data.room.gamebase.dao.PictureDao
import com.example.adygall2.data.room.gamebase.dao.SoundEffectDao
import com.example.adygall2.data.room.gamebase.dao.SoundsDao
import com.example.adygall2.data.room.gamebase.dao.TaskDao
import com.example.adygall2.data.room.gamebase.entities.AnswerEntity
import com.example.adygall2.data.room.gamebase.entities.OrderEntity
import com.example.adygall2.data.room.gamebase.entities.PictureEntity
import com.example.adygall2.data.room.gamebase.entities.SoundEffectEntity
import com.example.adygall2.data.room.gamebase.entities.SoundEntity
import com.example.adygall2.data.room.gamebase.entities.TaskEntity

/**
 * Реализация базы данных Room
 * */
@Database(
    entities = [
        AnswerEntity::class,
        OrderEntity::class,
        PictureEntity::class,
        TaskEntity::class,
        SoundEntity::class,
        SoundEffectEntity::class
    ], version = 6, exportSchema = false
)
abstract class GameBase : RoomDatabase() {

    companion object {
        fun buildDatabase(context : Context) =
            Room.databaseBuilder(context, GameBase::class.java, "game_database").
                    fallbackToDestructiveMigration().createFromAsset(FULL_BASE).build()
    }

    abstract fun getAnswerDao() : AnswerDao
    abstract fun getOrderDao() : OrderDao
    abstract fun getPictureDao() : PictureDao
    abstract fun getTaskDao() : TaskDao
    abstract fun getSoundsDao() : SoundsDao
    abstract fun getSoundsEffectDao() : SoundEffectDao
}