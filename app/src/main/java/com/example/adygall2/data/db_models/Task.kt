package com.example.adygall2.data.db_models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Task(
    val id : Int,

    val taskType : Int,

    val task : String,

    val soundId : Int,

    val levelId : Int,

    val lessonId : Int,

    val exerciseId : Int
    ) : Parcelable