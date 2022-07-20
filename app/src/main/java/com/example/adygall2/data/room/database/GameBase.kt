package com.example.adygall2.data.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.adygall2.data.room.consts.RoomConst.PATH_TO_LITE_DATABASE
import com.example.adygall2.data.room.dao.AnswerDao
import com.example.adygall2.data.room.dao.OrderDao
import com.example.adygall2.data.room.dao.PictureDao
import com.example.adygall2.data.room.dao.SoundsDao
import com.example.adygall2.data.room.dao.TaskDao
import com.example.adygall2.data.room.entities.AnswerEntity
import com.example.adygall2.data.room.entities.OrderEntity
import com.example.adygall2.data.room.entities.PictureEntity
import com.example.adygall2.data.room.entities.SoundEntity
import com.example.adygall2.data.room.entities.TaskEntity

@Database(
    entities = [
        AnswerEntity::class,
        OrderEntity::class,
        PictureEntity::class,
        TaskEntity::class,
        SoundEntity::class
    ], version = 3, exportSchema = false
)
abstract class GameBase : RoomDatabase() {

    companion object {
        fun buildDatabase(context : Context) =
            Room.databaseBuilder(context, GameBase::class.java, "game_database").
                    fallbackToDestructiveMigration().createFromAsset(PATH_TO_LITE_DATABASE).build()
    }

    abstract fun getAnswerDao() : AnswerDao
    abstract fun getOrderDao() : OrderDao
    abstract fun getPictureDao() : PictureDao
    abstract fun getTaskDao() : TaskDao
    abstract fun getSoundsDao() : SoundsDao
}