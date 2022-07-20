package com.example.adygall2.data.room.consts

object RoomConst {
    // Название базы данных
    private const val DATABASE_5_NAME = "game_v5.db"
    private const val DATABASE_7_NAME = "game_v7.db"
    private const val LITE_DATABASE_NAME = "lite_base.db"
    const val PATH_TO_DATABASE_5 = "game_db_alpha/$DATABASE_5_NAME"
    const val PATH_TO_DATABASE_7 = "game_db_alpha/$DATABASE_7_NAME"
    const val PATH_TO_LITE_DATABASE = "game_db_alpha/$LITE_DATABASE_NAME"


    // Названия таблиц
    const val ANSWER_TABLE_NAME = "answers"
    const val ORDER_TABLE_NAME = "order"
    const val PICTURES_TABLE_NAME = "pictures"
    const val TASKS_TABLE_NAME = "tasks"
    const val SOUNDS_TABLE_NAME = "sounds"

    // Названия полей
    const val ID = "id"
    const val TASK_ID = "task_id"
    const val ANSWER = "answer"
    const val PIC = "pic"
    const val CORRECT_ANSWER = "correct_answer"
    const val TASK = "task"
    const val TASK_NUM = "task_num"
    const val NAME = "name"
    const val TASK_TYPE = "task_type"
    const val SOUND = "sound"
}