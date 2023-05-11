package com.example.adygall2.data.room.consts

/**
 * Константные значения, связанные с бд
 */
object RoomConst {
    private const val DATABASE_DIRECTORY = "game_db_alpha"
    // Название базы данных
    const val FULL_BASE = "$DATABASE_DIRECTORY/game_base.db"
    const val SECOND_BASE = "$DATABASE_DIRECTORY/second_base.db"

    // Названия таблиц
    const val ANSWER_TABLE_NAME = "answers"
    const val ORDER_TABLE_NAME = "order"
    const val PICTURES_TABLE_NAME = "pictures"
    const val TASKS_TABLE_NAME = "tasks"
    const val SOUNDS_TABLE_NAME = "sounds"
    const val SOUND_EFFECT_TABLE_NAME = "sound_effect"

    const val USER_TABLE = "user"

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

    const val HP = "hp"
    const val COINS = "coins"
    const val IS_USER_SIGN_UP = "is_user_sign_up"
    const val LAST_ONLINE = "last_online"
    const val LEARNED_WORDS = "learned_words"
    const val LEARNING_PROGRESS = "learning_progress"
    const val GLOBAL_PLAYED_TIME = "global_played_time"
}