package com.example.adygall2.data.room.consts

/**
 * Типы заданий
 */
object TaskType {
    /** Не определён */
    const val UNKNOWN = 0
    /** Задание с картинками */
    const val IMAGE = 1
    /** Задание с выбором трёх слов */
    const val THREE_WORDS = 2
    /** Задание с построением предложения из услышанного */
    const val SENTENCE_BUILD = 3
    /** Задание с построением перевода предложения из текста */
    const val TRANSLATE_SENTENCE = 4
    /** Задание с набиранием услышанного на клавиатуре */
    const val TYPE_THAT_YOUR_HEARD = 5
    /** Задание с сопоставлением пар слов */
    const val SELECT_PAIRS_OF_WORDS = 6
    /** Задание с заполнением пропуска кнопкой */
    const val FILL_IN_THE_PASS = 7
    /** Задание с заполнением пропусов с клавиатуры */
    const val FILL_IN_THE_GAPS = 8
    /** Задание с набором перевода текста с клавиатуры */
    const val TYPE_TRANSLATE = 10
}