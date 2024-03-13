package com.example.adygall2.data.delegate

class AnswerFormatterImpl : AnswerFormatter {
    /** отрицание всех русских букв */
    private val notLetters = "[^а-яА-ЯЁёA-Za-z]+"

    /** Все пунктуационные знаки */
    private val punct = "\"W+"

    /** Знак пробела */
    private val whiteSpace = "\"s"

    /** Находит в строке все не буквы и все не цифры */
    private val complexSplit = "$punct$whiteSpace|$notLetters".toRegex()

    /**
     * @return Делит строку по указанному regex и возвращает String
     * @param str - строка, подаваемая для деления
     */
    override fun transform(str: String): String =
        str.lowercase()
            .trim()
            .split(complexSplit)
            .joinToString(separator = " ")
            .replace("ё", "е").replace("[1iLlI|]".toRegex(), "I")
            .trim()
}