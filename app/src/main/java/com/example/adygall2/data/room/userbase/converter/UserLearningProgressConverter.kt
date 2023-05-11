package com.example.adygall2.data.room.userbase.converter

import android.util.Log
import androidx.room.TypeConverter
import com.example.adygall2.data.room.userbase.ProgressItem

class UserLearningProgressConverter {
    @TypeConverter
    fun convertToString(progress: MutableSet<ProgressItem>): String {
        Log.i("user", "progress set = $progress")
        return progress.joinToString(separator = ", ") { "${it.level}-${it.lesson}" }
    }


    @TypeConverter
    fun convertToProgressItemSet(convertedString: String): MutableSet<ProgressItem> {
        Log.i("user", "string = $convertedString")
        return if (convertedString.isNotEmpty()) {
            convertedString.split(", ").map {
                val (level, lesson) = it.split("-")
                Log.i("user", "splited items = ${convertedString.split(", ")}")
                ProgressItem(
                    level = level.toInt(),
                    lesson = lesson.toInt()
                )
            }.toMutableSet()
        } else {
            mutableSetOf<ProgressItem>()
        }
    }
}