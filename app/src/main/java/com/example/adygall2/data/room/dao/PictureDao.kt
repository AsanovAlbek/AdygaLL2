package com.example.adygall2.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.adygall2.data.room.consts.RoomConst.PICTURES_TABLE_NAME
import com.example.adygall2.data.room.entities.AnswerEntity
import com.example.adygall2.data.room.entities.PictureEntity

/**
 * DAO для запросов, связанных с картинками [PictureEntity]
 */
@Dao
abstract class PictureDao {
    /** Получение всех [PictureEntity] */
    @Query("SELECT * FROM $PICTURES_TABLE_NAME")
    abstract fun getAllPictures() : List<PictureEntity>

    /** Получение [PictureEntity] по id */
    @Query("SELECT * FROM $PICTURES_TABLE_NAME WHERE id = :pictureId")
    abstract fun getPicture(pictureId : Int) : PictureEntity
}