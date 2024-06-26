package com.example.adygall2.data.room.gamebase.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.adygall2.data.room.consts.RoomConst.ANSWER
import com.example.adygall2.data.room.consts.RoomConst.ANSWER_TABLE_NAME
import com.example.adygall2.data.room.consts.RoomConst.CORRECT_ANSWER
import com.example.adygall2.data.room.consts.RoomConst.ID
import com.example.adygall2.data.room.consts.RoomConst.PIC
import com.example.adygall2.data.room.consts.RoomConst.SOUND
import com.example.adygall2.data.room.consts.RoomConst.TASK_ID

/**
 * Entity класс для хранения данных из таблицы 'answers'
 * @param id - id ответа
 * @param taskId - id задания
 * @param answer - текст ответа
 * @param isCorrectAnswer - правильный ответ (в некоторых типах заданий преобразуется в [Boolean])
 * @param pictureId - id картинки
 * @param soundId - id озвучки
 */
@Entity(tableName = ANSWER_TABLE_NAME)
data class AnswerEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    val id : Int = 0,

    @ColumnInfo(name = TASK_ID)
    val taskId : Int,

    @ColumnInfo(name = ANSWER)
    val answer : String,

    @ColumnInfo(name = CORRECT_ANSWER)
    val isCorrectAnswer : String,

    @ColumnInfo(name = PIC)
    val pictureId : Int,

    @ColumnInfo(name = SOUND)
    val soundId : Int
)
