package com.example.adygall2.domain.model

/** Класс для хранения ответа, его озвучки и его картинки
 * @param answer - ответ
 * @param answerPicture - картинка к ответу
 * @param answerSound - озвучка к ответу
 * */
data class ComplexAnswer(
    val answer: Answer,
    val answerSound : Source,
    val answerPicture : Source
)
