package com.example.adygall2.presentation.view_model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.adygall2.R
import com.example.adygall2.data.models.ResourceProvider
import com.example.adygall2.domain.model.Task
import com.example.adygall2.domain.usecases.GetAllOrdersUseCase
import com.example.adygall2.domain.usecases.TasksByOrdersUseCase
import com.example.adygall2.domain.usecases.UserSettingsUseCase
import com.example.adygall2.presentation.const.LastNavigationPage.HOME_SCREEN
import com.example.adygall2.presentation.fragments.menu.FragmentHomePageDirections
import java.io.File
import java.io.FileInputStream
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val userSettingsUseCase: UserSettingsUseCase,
    private val tasksByOrdersUseCase: TasksByOrdersUseCase,
    private val getAllOrdersUseCase: GetAllOrdersUseCase,
    private val resourceProvider: ResourceProvider,
    private val mainDispatcher: CoroutineDispatcher,
    private val ioDispatcher: CoroutineDispatcher
): ViewModel() {

    companion object {
        private const val fiveMinuteInMills = 300_000L // 5 минут * 60 секунд * 1000 милисекунд. 5 минут в милисекундах
    }

    val user get() = userSettingsUseCase.userInfo()

    /** Значение здоровья, восстанавливающегося во время сессии */
    private var _hpHill = MutableStateFlow(0)
    val hpHill get() = _hpHill.asStateFlow()

    /** Лист заданий из бд */
    private var _tasksListFromDb = MutableLiveData<List<Task>>()
    val tasksListFromDb : LiveData<List<Task>> get() = _tasksListFromDb

    fun saveUserStates(hp: Int, coins: Int) = userSettingsUseCase.updateUserInfo(
        userHp = hp,
        userCoins = coins
    )

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
        navController: NavController
    ) {
        val gameAction = FragmentHomePageDirections.actionHomePageToTaskContainer(
            levelProgress = level,
            lessonProgress = lesson,
            tasks = tasks.toTypedArray()
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

    fun getPhotoFromCache(): Bitmap {
        val directory =
            resourceProvider.contextWrapper.getDir(
                resourceProvider.getString(R.string.user_avatar),
                Context.MODE_PRIVATE
            )
        val saveFile = File(directory, "thumbnail.jpeg")

        if (saveFile.exists()) {
            FileInputStream(saveFile).use { inputStream ->
                return BitmapFactory.decodeStream(inputStream)
            }
        } else {
            return resourceProvider.getBitmap(R.drawable.default_avatar)!!.toBitmap()
        }
    }

    override fun onCleared() {
        userSettingsUseCase.updateUserInfo(userLastFragment = HOME_SCREEN)
        super.onCleared()
    }
}