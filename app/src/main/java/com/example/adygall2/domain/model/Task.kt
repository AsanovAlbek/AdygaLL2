package com.example.adygall2.domain.model

/**
 * Модель для заданий
 * @param id - id задания
 * @param taskType - тип задания (типы приведены в [TaskType])
 * @param task - текст вопроса
 * @param levelId - id уровня
 * @param lessonId - id урока
 * @param exerciseId - id внутри одного урока из 15 вопросов
 */
data class Task(
    val id : Int,
    val taskType : Int,
    val task : String,
    val soundId : Int,
    val levelId : Int,
    val lessonId : Int,
    val exerciseId : Int
    )