package com.example.adygall2.presentation

import androidx.lifecycle.*
import com.example.adygall2.data.db_models.*
import com.example.adygall2.domain.usecases.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameViewModel(
    private val answerUseCase: AnswerUseCase,
    private val orderUseCase: OrderUseCase,
    private val pictureUseCase: PictureUseCase,
    private val taskUseCase: TaskUseCase,
    private val soundUseCase: SoundUseCase
) : ViewModel() {

    private var _tasksListFromDb = MutableLiveData<List<Task>>()
    val tasksListFromDb : LiveData<List<Task>> get() = _tasksListFromDb

    private var _answersListFromDb = MutableLiveData<List<Answer>>()
    val answersListFromDb : LiveData<List<Answer>> get() = _answersListFromDb

    private var _picturesListByAnswersFromDb = MutableLiveData<List<Picture>>()
    val picturesListByAnswersFromDb : LiveData<List<Picture>> get() = _picturesListByAnswersFromDb

    private var _soundsListByTaskIdFromDb = MutableLiveData<List<Sound>>()
    val soundsListByTaskIdFromDb : LiveData<List<Sound>> get() = _soundsListByTaskIdFromDb

    private var _soundFromDb = MutableLiveData<Sound>()
    val soundFromDb : LiveData<Sound> get() = _soundFromDb

    fun getSoundById(soundId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val sound = soundUseCase.getSoundById(soundId)
                withContext(Dispatchers.Main) {
                    _soundFromDb.value = sound
                }
            }
        }
    }

    fun getAnswers(taskId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val answers = answerUseCase.getAnswersByTaskId(taskId)
                withContext(Dispatchers.Main) {
                    _answersListFromDb.value = answers
                }
            }
        }
    }

    fun getPicturesByAnswers(answers : List<Answer>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val pictures = pictureUseCase.getPicturesByAnswers(answers)
                withContext(Dispatchers.Main) {
                    _picturesListByAnswersFromDb.value = pictures
                }
            }
        }
    }

    fun getTasksFromOrder() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val tasks = taskUseCase.getTasksFromOrders(orderUseCase.getAllOrders())
                withContext(Dispatchers.Main) {
                    _tasksListFromDb.value = tasks
                }
            }
        }
    }
}