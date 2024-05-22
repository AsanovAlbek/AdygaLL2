package com.example.adygall2.presentation.view_model

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.adygall2.R
import com.example.adygall2.data.models.ResourceProvider
import com.example.adygall2.domain.model.Task
import com.example.adygall2.domain.model.User
import com.example.adygall2.domain.usecases.GetAllOrdersUseCase
import com.example.adygall2.domain.usecases.TasksByLessonUseCase
import com.example.adygall2.domain.usecases.TasksByOrdersUseCase
import com.example.adygall2.domain.usecases.UserUseCase
import com.example.adygall2.presentation.fragments.main.FragmentHomePageDirections
import com.example.adygall2.presentation.model.HomeState
import java.util.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val userUseCase: UserUseCase,
    private val tasksByLessonUseCase: TasksByLessonUseCase,
    private val resourceProvider: ResourceProvider,
    private val mainDispatcher: CoroutineDispatcher,
    private val ioDispatcher: CoroutineDispatcher
): ViewModel() {

    companion object {
        private const val fiveMinuteInMills = 300_000L // 5 минут * 60 секунд * 1000 милисекунд. 5 минут в милисекундах
    }

    var user: User = User()

    private val userLiveData = MutableLiveData<User>()
    val observableUser: LiveData<User> get() = userLiveData

    /** Лист заданий из бд */
    private var _homeState = MutableLiveData<HomeState>(HomeState())
    val homeState : LiveData<HomeState> get() = _homeState
    private var homeStateTemp = HomeState()

    init {
        viewModelScope.launch {
            withContext(mainDispatcher) {
                user = userUseCase.getUser()

                Log.i("user", "user from db = $user")
                userLiveData.value = user
            }
        }
    }

    fun saveUserStates(hp: Int, coins: Int) {
        viewModelScope.launch {
            withContext(mainDispatcher) {
                if (Date().time - user.lastOnlineTimeInMillis >= 300_000) {
                    user = user.copy(
                        lastOnlineTimeInMillis = Date().time
                    )
                }
                user = user.copy(
                    hp = hp,
                    coins = coins
                )
                userLiveData.value = user
                userUseCase.updateUser(user)
            }
        }
    }

    fun openLesson(
        level: Int,
        lesson: Int,
        navController: NavController,
        levelName: String
    ) {
        val gameAction = FragmentHomePageDirections.actionHomePageToTaskContainer(
            levelProgress = level,
            lessonProgress = lesson,
            levelName = levelName
        )
        navController.navigate(gameAction)
    }

    fun noHpMessage(context: Context) {
        AlertDialog.Builder(context)
            .setMessage(R.string.no_hp)
            .setNeutralButton(R.string.ok) { dialog, _ -> dialog.cancel() }
            .show()
    }

    /** Получение заданий в очерёдности, заданной в orders */
    fun getTasksFromOrder() {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                val levelsAndLessons = tasksByLessonUseCase.allLessons()
                homeStateTemp = homeStateTemp.copy(loading = false, levelsAndLessons = levelsAndLessons)
                withContext(mainDispatcher) {
                    _homeState.value = homeStateTemp
                }
            }
        }
    }

    fun getPhotoFromCache(): Bitmap = userUseCase.getUserImage(resourceProvider = resourceProvider)
}