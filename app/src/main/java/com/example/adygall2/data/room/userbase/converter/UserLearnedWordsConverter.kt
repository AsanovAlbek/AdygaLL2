package com.example.adygall2.data.room.userbase.converter

import androidx.room.TypeConverter

class UserLearnedWordsConverter {
    @TypeConverter
    fun convertToString(words: MutableSet<String>): String =
        words.joinToString()

    @TypeConverter
    fun convertToStringSet(convertedSet: String): MutableSet<String> =
        if (convertedSet.isNotEmpty()) {
            convertedSet.split(", ").toMutableSet()
        } else {
            mutableSetOf<String>()
        }
}