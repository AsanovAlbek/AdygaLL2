package com.example.adygall2.presentation.adapters.groupieitems.model

sealed class LessonCompleteState {
    data class Completed(
        val completedMark: String = "✓",
        val progress: Int = 100
    ) : LessonCompleteState()

    data class Unlocked(
        val lessonNumber: Int = 1,
        val progress: Int = 0
    ): LessonCompleteState()

    data class Locked(
        val lessonNumber: Int = 1
    ): LessonCompleteState()

    fun LessonCompleteState.Unlocked.progressUp() = this.copy(progress = progress + 25)
}