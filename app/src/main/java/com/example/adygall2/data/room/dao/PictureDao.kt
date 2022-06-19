package com.example.adygall2.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.adygall2.data.room.consts.RoomConst.PICTURES_TABLE_NAME
import com.example.adygall2.data.room.entities.PictureEntity

@Dao
abstract class PictureDao {
    @Query("SELECT * FROM $PICTURES_TABLE_NAME")
    abstract fun getAllPictures() : List<PictureEntity>

    @Query("SELECT * FROM $PICTURES_TABLE_NAME WHERE id = :pictureId")
    abstract fun getPicture(pictureId : Int) : PictureEntity
}