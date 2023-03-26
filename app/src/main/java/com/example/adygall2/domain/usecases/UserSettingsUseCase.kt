package com.example.adygall2.domain.usecases

import com.example.adygall2.data.models.settings.ProgressItem
import com.example.adygall2.data.models.settings.UserSettings

class UserSettingsUseCase(
    private val userSettings: UserSettings
    ) {
    fun userInfo() = userSettings.userInfo()

    fun updateUserInfo(
        userHp: Int? = null,
        userCoins: Int? = null,
        name: String? = null,
        userIsSignUp: Boolean? = null,
        userLastOnlineTime: Long? = null,
        learnedWords: Set<String>? = null,
        userLearningProgressSet: Set<ProgressItem>? = null,
        userLastFragment: Int? = null
    ) = userSettings.updateUserInfo(
            userHp = userHp,
            userCoins = userCoins,
            name = name,
            userIsSignUp = userIsSignUp,
            userLastOnlineTime = userLastOnlineTime,
            learnedWords = learnedWords,
            userLearningProgressSet = userLearningProgressSet,
            lastSavedFragment = userLastFragment
        )

    fun addLessonToCompletedLessons(level: Int, lesson: Int) = userSettings.addCompletedLesson(level, lesson)
}