package com.example.adygall2.data.models.settings

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.adygall2.data.local.PrefConst.DATE
import com.example.adygall2.data.local.PrefConst.IS_USER_SIGN_UP
import com.example.adygall2.data.local.PrefConst.LAST_USER_FRAGMENT
import com.example.adygall2.data.local.PrefConst.LEARNED_WORDS
import com.example.adygall2.data.local.PrefConst.USER_EXP
import com.example.adygall2.data.local.PrefConst.USER_HP
import com.example.adygall2.data.local.PrefConst.USER_NAME
import com.example.adygall2.data.local.PrefConst.USER_PROGRESS
import com.example.adygall2.presentation.const.LastNavigationPage
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

object UserSettings: KoinComponent {

    private const val DEFAULT_USER_NAME = "no name"

    /** Сохранение и получение здоровья пользователя */
    private val userHealth by inject<SharedPreferences>(named(USER_HP))
    /** Сохранение и получение монет пользователя */
    private val userMoney by inject<SharedPreferences>(named(USER_EXP))
    /** Сохранение и получение прогресса пользователя */
    private val userProgressSet by inject<SharedPreferences>(named(USER_PROGRESS))
    /** Сохранение и получение имени пользователя */
    private val userName by inject<SharedPreferences>(named(USER_NAME))
    /** Сохранение и получение выученных пользователя */
    private val userLearnedWords by inject<SharedPreferences>(named(LEARNED_WORDS))
    /** Сохранение и получение времени последнего входа пользователя */
    private val lastUserOnlineDate by inject<SharedPreferences>(named(DATE))
    /** Сохранение и получение статуса зарегистрированности пользователя */
    private val isUserSignUp by inject<SharedPreferences>(named(IS_USER_SIGN_UP))
    /** Последний экран, на котором был пользователь */
    private val lastUserFragment by inject<SharedPreferences>(named(LAST_USER_FRAGMENT))

    fun userInfo(): UserInfo =
        UserInfo(
            hp = userHealth.getInt(USER_HP, 100),
            coins = userMoney.getInt(USER_EXP, 0),
            name = userName.getString(USER_NAME, DEFAULT_USER_NAME) ?: DEFAULT_USER_NAME,
            isUserSignUp = isUserSignUp.getBoolean(IS_USER_SIGN_UP, false),
            lastUserOnline = lastUserOnlineDate.getLong(DATE, 0L),
            learnedWords = userLearnedWords.getStringSet(LEARNED_WORDS, mutableSetOf<String>()) ?: mutableSetOf(),
            learningProgressSet = userProgressSet.getStringSet(USER_PROGRESS, mutableSetOf<String>())
                ?.map {
                val (level, lesson) = it.split("-")
                ProgressItem(level = level.toInt(), lesson = lesson.toInt())
            }?.toMutableSet() ?: mutableSetOf(),
            lastFragmentNum = lastUserFragment.getInt(LAST_USER_FRAGMENT, 1)
        )

    fun updateUserInfo(
        userHp: Int? = null,
        userCoins: Int? = null,
        name: String? = null,
        userIsSignUp: Boolean? = null,
        userLastOnlineTime: Long? = null,
        learnedWords: Set<String>? = null,
        userLearningProgressSet: Set<ProgressItem>? = null,
        lastSavedFragment: Int? = null
    ) {
        userHp?.let { userHealth.edit { putInt(USER_HP, it) } }
        userCoins?.let { userMoney.edit { putInt(USER_EXP, it) } }
        learnedWords?.let { userLearnedWords.edit { putStringSet(LEARNED_WORDS, it) } }
        name?.let { userName.edit { putString(USER_NAME, it) } }
        userIsSignUp?.let { isUserSignUp.edit { putBoolean(IS_USER_SIGN_UP, it) } }
        userLastOnlineTime?.let { lastUserOnlineDate.edit { putLong(DATE, it) } }
        userLearningProgressSet?.let { progressSet ->
            val progressItemToStringSet = progressSet.map { "${it.level}-${it.lesson}" }.toSet()
            userProgressSet.edit { putStringSet(USER_PROGRESS, progressItemToStringSet)
            }
        }
        lastSavedFragment?.let { lastUserFragment.edit { putInt(LAST_USER_FRAGMENT,it) } }
    }

    fun addCompletedLesson(level: Int, lesson: Int) {
        val progressSet = userInfo().learningProgressSet.toMutableSet()
        progressSet.add(ProgressItem(level, lesson))
        updateUserInfo(userLearningProgressSet = progressSet)
    }
}

data class UserInfo(
    val hp: Int = 100,
    val coins: Int = 0,
    val name: String = "no name",
    val isUserSignUp: Boolean = false,
    val lastUserOnline: Long = 0L,
    val learnedWords: MutableSet<String> = mutableSetOf(),
    val learningProgressSet: MutableSet<ProgressItem> = mutableSetOf(),
    val lastFragmentNum: Int = 1
)

data class ProgressItem(
    val level: Int = 0,
    val lesson: Int = 0
)