package com.example.adygall2.data.room.gamebase.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.adygall2.data.room.consts.RoomConst.ANSWER_TABLE_NAME
import com.example.adygall2.data.room.gamebase.entities.AnswerEntity

/**
 * DAO для запросов, связанных с ответами [AnswerEntity]
 */
@Dao
abstract class AnswerDao {
    /** Получение всех ответов по id задания */
    @Query("SELECT * FROM $ANSWER_TABLE_NAME WHERE task_id = :taskId")
    abstract fun getTaskAnswers(taskId : Int) : List<AnswerEntity>

    /** Получение всех ответов [AnswerEntity] */
    @Query("SELECT * FROM $ANSWER_TABLE_NAME")
    abstract fun getAllAnswers() : List<AnswerEntity>
}