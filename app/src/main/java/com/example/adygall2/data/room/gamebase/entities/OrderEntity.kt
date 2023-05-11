package com.example.adygall2.data.room.gamebase.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.adygall2.data.room.consts.RoomConst.ID
import com.example.adygall2.data.room.consts.RoomConst.ORDER_TABLE_NAME
import com.example.adygall2.data.room.consts.RoomConst.TASK_NUM

/**
 * Entity класс для хранения данных из таблицы 'orders'
 * @param id - id ордера
 * @param taskNum - id задания
 */
@Entity(tableName = ORDER_TABLE_NAME)
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    val id : Int = 0,

    @ColumnInfo(name = TASK_NUM)
    val taskNum : Int
)
