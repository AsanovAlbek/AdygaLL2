package com.example.adygall2.presentation.view_model

import androidx.lifecycle.*
import com.example.adygall2.data.db_models.*
import com.example.adygall2.data.delegate.AnswerHelper
import com.example.adygall2.data.delegate.AnswerHelperImpl
import com.example.adygall2.domain.usecases.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameViewModel(
    private val answerUseCase: AnswerUseCase,
    private val orderUseCase: OrderUseCase,
    private val pictureUseCase: PictureUseCase,
    private val taskUseCase: TaskUseCase,
    private val soundUseCase: SoundUseCase,
    private val soundEffectUseCase: SoundEffectUseCase,
    private val answerHelperDelegate : AnswerHelper
) : ViewModel(), AnswerHelper by answerHelperDelegate {

    private var _tasksListFromDb = MutableLiveData<List<Task>>()
    val tasksListFromDb : LiveData<List<Task>> get() = _tasksListFromDb

    private var _answersListFromDb = MutableLiveData<MutableList<Answer>>()
    val answersListFromDb : LiveData<MutableList<Answer>> get() = _answersListFromDb

    private var _picturesListByAnswersFromDb = MutableLiveData<List<Picture>>()
    val picturesListByAnswersFromDb : LiveData<List<Picture>> get() = _picturesListByAnswersFromDb

    private var _soundsListByAnswersFromDb = MutableLiveData<List<Sound>>()
    val soundsListByAnswersFromDb : LiveData<List<Sound>> get() = _soundsListByAnswersFromDb

    private var _soundFromDb = MutableLiveData<Sound>()
    val soundFromDb : LiveData<Sound> get() = _soundFromDb

    private var _goodSoundEffect = MutableLiveData<SoundEffect>()
    val goodSoundEffect : LiveData<SoundEffect> get() = _goodSoundEffect

    private var _badSoundEffect = MutableLiveData<SoundEffect>()
    val badSoundEffect : LiveData<SoundEffect> get() = _badSoundEffect

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

    fun okEffect() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val rightAnswerEffect = soundEffectUseCase.rightAnswerEffect()
                withContext(Dispatchers.Main) {
                    _goodSoundEffect.value = rightAnswerEffect
                }
            }
        }
    }

    fun wrongEffect() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val wrongAnswerEffect = soundEffectUseCase.wrongAnswerEffect()
                withContext(Dispatchers.Main) {
                    _badSoundEffect.value = wrongAnswerEffect
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

    fun getSoundsByAnswers(answers: List<Answer>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val sounds = soundUseCase.getSoundsByAnswers(answers)
                withContext(Dispatchers.Main) {
                    _soundsListByAnswersFromDb.value = sounds
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