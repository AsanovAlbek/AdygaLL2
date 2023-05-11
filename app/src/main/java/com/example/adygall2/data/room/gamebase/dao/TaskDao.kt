package com.example.adygall2.data.room.gamebase.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.adygall2.data.room.consts.RoomConst.TASKS_TABLE_NAME
import com.example.adygall2.data.room.gamebase.entities.TaskEntity

/**
 * DAO для запросов, связанных с заданиями [TaskEntity]
 */
@Dao
abstract class TaskDao {
    /** Метод для получения задания по типу задания */
    @Query("SELECT * FROM $TASKS_TABLE_NAME WHERE task_type = :taskType")
    abstract fun getTaskByType(taskType : Int) : List<TaskEntity>

    /** Метод для получения задания по id */
    @Query("SELECT * FROM $TASKS_TABLE_NAME WHERE id = :taskId")
    abstract fun getTaskById(taskId : Int) : TaskEntity
}