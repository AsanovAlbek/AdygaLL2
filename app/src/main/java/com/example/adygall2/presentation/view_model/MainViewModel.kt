package com.example.adygall2.presentation.view_model

import android.app.Activity
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.adygall2.R
import com.example.adygall2.data.models.ResourceProvider
import com.example.adygall2.domain.model.User
import com.example.adygall2.domain.usecases.UserUseCase
import com.example.adygall2.presentation.model.UserProfileState
import java.util.Date
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MainViewModel(
    private val userUseCase: UserUseCase,
    private val resourceProvider: ResourceProvider,
    private val mainDispatcher: CoroutineDispatcher,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    companion object {
        // Время в минутах, за которое увеличивается здоровье на 10
        const val HEALTH_REGENERATE_VALUE = 10
        const val HEALTH_REGENERATE_TIME = 5
        const val MILLIS_IN_SECOND = 1000
        const val SECOND_IN_MINUTE = 60
        const val MAX_HP = 100
    }

    private var tempUser = User()
    val user get() = tempUser

    init {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                Log.i("user", "user exists fun return ${userUseCase.isUserExist()}")
                tempUser = if (userUseCase.isUserExist()) userUseCase.getUser() else User()
                Log.i("user", "user from db = $tempUser")
                Log.i("user", "user exists is ${tempUser.isUserSignUp}")
                current = current.copy(
                    name = tempUser.name,
                    photo = userUseCase.getUserImage(resourceProvider = resourceProvider),
                    isUserSignUp = userUseCase.isUserLogIn()
                )
                withContext(mainDispatcher) {
                    userState.value = current
                }
            }
        }
    }

    private val userState = MutableLiveData<UserProfileState>()
    val state: LiveData<UserProfileState> get() = userState
    private var current = UserProfileState()

    fun isUserLogIn() = userUseCase.isUserLogIn()

    fun userChange(user: User) {
        viewModelScope.launch {
            withContext(mainDispatcher) {
                tempUser = user
                current = current.copy(
                    name = user.name,
                    photo = userUseCase.getUserImage(resourceProvider = resourceProvider),
                    isUserSignUp = true
                )
                userState.value = current
            }
        }
    }

    fun regenerateHealthOffline() {
        viewModelScope.launch {
            withContext(mainDispatcher) {
                var userHealth = user.hp
                // Разница во времени между выходом и в
                val now = Date()
                // Время последнего выхода
                val lastExitTimeMillis = user.lastOnlineTimeInMillis
                // Значение здоровья пользователяходом
                val period = now.time - lastExitTimeMillis
                // В минутах
                val minutes = period / MILLIS_IN_SECOND / SECOND_IN_MINUTE
                // Высчитываем, сколько раз прошло по 5 минут и восстанавливаем здоровье на 10 за каждые 5 минут
                userHealth += ((minutes / HEALTH_REGENERATE_TIME).toInt() * HEALTH_REGENERATE_VALUE)
                // Если получилось значение больше 100, то делаем значение 100
                if (userHealth > MAX_HP) userHealth = MAX_HP
                // Записываем здоровье в SharedPreference
                tempUser = tempUser.copy(hp = userHealth)
                userUseCase.updateUser(tempUser)
                tempUser = userUseCase.getUser()
            }
        }
    }

    fun saveExitTime() {
        viewModelScope.launch {
            withContext(mainDispatcher) {
                tempUser = tempUser.copy(lastOnlineTimeInMillis = Date().time)
                Log.i("user", "save $tempUser and exit")
                userUseCase.updateUser(tempUser)
            }
        }
    }

    fun exitFromAppDialog(activity: Activity) {
        AlertDialog.Builder(activity)
            .setMessage(resourceProvider.getString(R.string.exit_from_app_question))
            .setPositiveButton(resourceProvider.getString(R.string.yes)) { _, _ ->
                saveExitTime()
                activity.finish()
            }
            .setNegativeButton(resourceProvider.getString(R.string.no)) { dialog, _ -> dialog.cancel() }
            .create()
            .show()
    }

    fun exitFromLessonDialog(activity: Activity, navController: NavController) {
        AlertDialog.Builder(activity)
            .setMessage(R.string.exit_from_level_question)
            .setPositiveButton(R.string.yes) { _, _ ->
                navController.navigate(R.id.homePage)
            }
            .setNeutralButton(R.string.no) { dialog, _ -> dialog.cancel() }
            .create()
            .show()
    }
}