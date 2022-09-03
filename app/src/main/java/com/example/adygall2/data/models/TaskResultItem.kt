package com.example.adygall2.data.models

/**
 * Модель для хранения результатов заданий
 * Пока не реализованно
 *
 * @param taskNumber - номер (id) задания
 * @param task - текст задания
 * @param rightAnswer - правильный ответ
 * @param userAnswer - ответ пользователя
 */
data class TaskResultItem(
    val taskNumber : Int,
    val task : String,
    val rightAnswer : String,
    val userAnswer : String
)