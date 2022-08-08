package com.example.adygall2.data.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.adygall2.data.room.consts.RoomConst.FULL_DATABASE_3_PATH
import com.example.adygall2.data.room.consts.RoomConst.GAME_BASE_PATH
import com.example.adygall2.data.room.dao.AnswerDao
import com.example.adygall2.data.room.dao.OrderDao
import com.example.adygall2.data.room.dao.PictureDao
import com.example.adygall2.data.room.dao.SoundEffectDao
import com.example.adygall2.data.room.dao.SoundsDao
import com.example.adygall2.data.room.dao.TaskDao
import com.example.adygall2.data.room.entities.AnswerEntity
import com.example.adygall2.data.room.entities.OrderEntity
import com.example.adygall2.data.room.entities.PictureEntity
import com.example.adygall2.data.room.entities.SoundEffectEntity
import com.example.adygall2.data.room.entities.SoundEntity
import com.example.adygall2.data.room.entities.TaskEntity

@Database(
    entities = [
        AnswerEntity::class,
        OrderEntity::class,
        PictureEntity::class,
        TaskEntity::class,
        SoundEntity::class,
        SoundEffectEntity::class
    ], version = 5, exportSchema = false
)
abstract class GameBase : RoomDatabase() {

    companion object {
        fun buildDatabase(context : Context) =
            Room.databaseBuilder(context, GameBase::class.java, "game_database").
                    fallbackToDestructiveMigration().createFromAsset(GAME_BASE_PATH).build()
    }

    abstract fun getAnswerDao() : AnswerDao
    abstract fun getOrderDao() : OrderDao
    abstract fun getPictureDao() : PictureDao
    abstract fun getTaskDao() : TaskDao
    abstract fun getSoundsDao() : SoundsDao
    abstract fun getSoundsEffectDao() : SoundEffectDao
}