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
import com.example.adygall2.domain.usecases.TasksByOrdersUseCase
import com.example.adygall2.domain.usecases.UserUseCase
import com.example.adygall2.presentation.fragments.main.FragmentHomePageDirections
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val userUseCase: UserUseCase,
    private val tasksByOrdersUseCase: TasksByOrdersUseCase,
    private val getAllOrdersUseCase: GetAllOrdersUseCase,
    private val resourceProvider: ResourceProvider,
    private val mainDispatcher: CoroutineDispatcher,
    private val ioDispatcher: CoroutineDispatcher
): ViewModel() {

    companion object {
        private const val fiveMinuteInMills = 300_000L // 5 минут * 60 секунд * 1000 милисекунд. 5 минут в милисекундах
    }

    lateinit var user: User

    /** Значение здоровья, восстанавливающегося во время сессии */
    private var _hpHill = MutableStateFlow(0)
    val hpHill get() = _hpHill.asStateFlow()

    private val userLiveData = MutableLiveData<User>()
    val observableUser: LiveData<User> get() = userLiveData

    /** Лист заданий из бд */
    private var _tasksListFromDb = MutableLiveData<List<Task>>()
    val tasksListFromDb : LiveData<List<Task>> get() = _tasksListFromDb

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
                user = user.copy(
                    hp = hp,
                    coins = coins
                )
                userLiveData.value = user
                userUseCase.updateUser(user)
            }
        }
    }

    /** получение последнего сохраненного значения здоровья */
    fun initAutoHill(value: Int) {
        viewModelScope.launch {
            withContext(mainDispatcher) {
                _hpHill.value = value
            }
        }
    }

    /** функция процесса восстановления здоровья*/
    fun autoHillHp() {
        viewModelScope.launch {
            withContext(mainDispatcher) {
                while (isActive) {
                    delay(fiveMinuteInMills)
                    if (_hpHill.value < 100) {
                        _hpHill.value += 10
                    }
                }
            }
        }
    }

    fun openLesson(
        level: Int,
        lesson: Int,
        tasks: List<Task>,
        navController: NavController,
        levelName: String
    ) {
        val gameAction = FragmentHomePageDirections.actionHomePageToTaskContainer(
            levelProgress = level,
            lessonProgress = lesson,
            tasks = tasks.toTypedArray(),
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
                val tasks = tasksByOrdersUseCase(getAllOrdersUseCase())
                withContext(mainDispatcher) {
                    _tasksListFromDb.value = tasks
                }
            }
        }
    }

    fun getPhotoFromCache(): Bitmap = userUseCase.getUserImage(resourceProvider = resourceProvider)
}