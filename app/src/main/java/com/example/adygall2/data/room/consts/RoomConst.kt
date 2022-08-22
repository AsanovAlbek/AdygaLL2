package com.example.adygall2.data.room.consts

object RoomConst {
    private const val DATABASE_DIRECTORY = "game_db_alpha"
    // Название базы данных
    const val DEMO_BASE = "$DATABASE_DIRECTORY/demo.db"

    // Названия таблиц
    const val ANSWER_TABLE_NAME = "answers"
    const val ORDER_TABLE_NAME = "order"
    const val PICTURES_TABLE_NAME = "pictures"
    const val TASKS_TABLE_NAME = "tasks"
    const val SOUNDS_TABLE_NAME = "sounds"
    const val SOUND_EFFECT_TABLE_NAME = "sound_effect"

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
    const val EFFECT = "effect"
    const val LEVEL = "level"
    const val LESSON = "lesson"
    const val EXERCISE = "exercise"
}