package com.example.adygall2.presentation.fragments.tasks.base_task

import androidx.fragment.app.Fragment

/**
 * Кастомный базовый фрагмент для фрагментов - заданий
 */
open class BaseTaskFragment(contentId : Int) : Fragment(contentId) {
    open val userAnswer = ""
    open val rightAnswer = ""
}