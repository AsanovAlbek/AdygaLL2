package com.example.adygall2.data.delegate

class AnswerHelperImpl : AnswerHelper {
    // отрицание всех русские буквы
    private val notRusLetters = "[^а-яА-ЯЁё]+"
    // Все пунктуационные знаки
    private val punct = "\"W+"
    private val whiteSpace = "\"s"
    // Находит в строке все не буквы и все не цифры
    private val complexSplit = "$punct$whiteSpace|$notRusLetters".toRegex()

    /**
     * @return Делит строку по указанному regex и возвращает String
     * @param str - строка, подаваемая для деления
     */
    override fun transform(str: String): String =
        str.lowercase().split(complexSplit).joinToString()
}