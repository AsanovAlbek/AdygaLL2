package com.example.adygall2.domain

import android.util.Log
import com.example.adygall2.domain.model.Task

fun List<Task>.levels(): Map<Int, List<Task>> = groupBy { it.levelId }

/** Находит урок по id
 * Использовать только после level!*/
fun List<Task>.lesson(lessonId: Int): List<Task> =
    this.filter { task -> task.lessonId == lessonId }

/** Нахождение среди уровней уровня с указанным id */
fun Map<Int, List<Task>>.level(levelId: Int): List<Task> = this[levelId]!!

/** Находит задание по id
 * Использовать только после lesson!*/
fun List<Task>.exercise(exerciseId: Int): Task = this.first { task -> task.exerciseId == exerciseId }

fun List<Task>.getTask(levelId: Int = 1, lessonId: Int = 1, exerciseId: Int = 1): Task =
    levels().level(levelId).lesson(lessonId).exercise(exerciseId)