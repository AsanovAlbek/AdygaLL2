package com.example.adygall2.domain.model

/**
 * Модель вариантов ответов из бд
 * @param id - id варианта ответа
 * @param taskId - id задания
 * @param answer - текст варианта ответа
 * @param correctAnswer - правильный ответ (в некоторых заданиях приводится к Boolean)
 * @param pictureId - id картинки, соответсвующей варианту ответа (при отсутсвии картинки id = 0)
 * @param soundId - id озвучки ответа (при отсутствии озвучки id = 0)
 */
data class Answer(
    val id : Int,
    val taskId : Int,
    val answer : String,
    val correctAnswer : String,
    val pictureId : Int,
    val soundId : Int
)
