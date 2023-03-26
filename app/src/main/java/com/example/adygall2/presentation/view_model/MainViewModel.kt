package com.example.adygall2.presentation.view_model

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.adygall2.R
import com.example.adygall2.data.models.ResourceProvider
import com.example.adygall2.data.models.settings.UserInfo
import com.example.adygall2.domain.usecases.UserSettingsUseCase
import java.util.Date
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val userSettingsUseCase: UserSettingsUseCase,
    private val resourceProvider: ResourceProvider,
    private val mainDispatcher: CoroutineDispatcher,
    private val ioDispatcher: CoroutineDispatcher
): ViewModel() {

    companion object {
        // Время в минутах, за которое увеличивается здоровье на 10
        const val HEALTH_REGENERATE_VALUE = 10
        const val HEALTH_REGENERATE_TIME = 5
        const val MILLIS_IN_SECOND = 1000
        const val SECOND_IN_MINUTE = 60
        const val MAX_HP = 100
    }

    val user: UserInfo get()  = userSettingsUseCase.userInfo()

    fun regenerateHealthOffline() {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                val now = Date()
                // Время последнего выхода
                val lastExitTimeMillis = user.lastUserOnline
                // Значение здоровья пользователя
                var userHealth = user.hp
                // Разница во времени между выходом и входом
                val period = now.time - lastExitTimeMillis
                // В минутах
                val minutes = period / MILLIS_IN_SECOND / SECOND_IN_MINUTE
                // Высчитываем, сколько раз прошло по 5 минут и восстанавливаем здоровье на 10 за каждые 5 минут
                userHealth += ((minutes / HEALTH_REGENERATE_TIME).toInt() * HEALTH_REGENERATE_VALUE)
                // Если получилось значение больше 100, то делаем значение 100
                if (userHealth > MAX_HP) userHealth = MAX_HP
                // Записываем здоровье в SharedPreference
                withContext(mainDispatcher) {
                    userSettingsUseCase.updateUserInfo(userHp = userHealth)
                }
            }
        }
    }

    fun saveExitTime() {
        viewModelScope.launch {
            withContext(mainDispatcher) {
                userSettingsUseCase.updateUserInfo(userLastOnlineTime = Date().time)
            }
        }
    }

    fun backPressedAlertDialog(activity: Activity) {
        AlertDialog.Builder(activity)
            .setMessage(resourceProvider.getString(R.string.exit_from_app_question))
            .setPositiveButton(resourceProvider.getString(R.string.yes)) { _, _ -> activity.finish() }
            .setNegativeButton(resourceProvider.getString(R.string.no)) { dialog, _ -> dialog.cancel() }
            .create()
            .show()
    }
}