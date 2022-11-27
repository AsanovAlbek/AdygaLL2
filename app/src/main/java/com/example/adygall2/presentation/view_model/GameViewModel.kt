package com.example.adygall2.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adygall2.data.delegate.AnswerFormatter
import com.example.adygall2.domain.model.Answer
import com.example.adygall2.domain.model.ComplexAnswer
import com.example.adygall2.domain.model.Source
import com.example.adygall2.domain.model.Task
import com.example.adygall2.domain.usecases.AnswersByTaskIdUseCase
import com.example.adygall2.domain.usecases.GetAllOrdersUseCase
import com.example.adygall2.domain.usecases.GetComplexAnswerUseCase
import com.example.adygall2.domain.usecases.SourceInteractor
import com.example.adygall2.domain.usecases.TasksByOrdersUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Вью модель для взаимодействия с данными из бд
 */
class GameViewModel(
    private val answersByTaskIdUseCase: AnswersByTaskIdUseCase,
    private val getAllOrdersUseCase: GetAllOrdersUseCase,
    private val tasksByOrdersUseCase: TasksByOrdersUseCase,
    private val getComplexAnswerUseCase: GetComplexAnswerUseCase,
    private val sourceInteractor: SourceInteractor,
    private val answerFormatterDelegate : AnswerFormatter
) : ViewModel(), AnswerFormatter by answerFormatterDelegate {

    /** Лист заданий из бд */
    private var _tasksListFromDb = MutableLiveData<List<Task>>()
    val tasksListFromDb : LiveData<List<Task>> get() = _tasksListFromDb

    /** Лист ответов из бд */
    private var _answersListFromDb = MutableLiveData<MutableList<Answer>>()
    val answersListFromDb : LiveData<MutableList<Answer>> get() = _answersListFromDb

    private var _complexAnswersListFromDb = MutableLiveData<List<ComplexAnswer>>()
    val complexAnswersListFromDb get() = _complexAnswersListFromDb

    /** Лист картинок из бд */
    private var _picturesListByAnswersFromDb = MutableLiveData<List<Source>>()
    val picturesListByAnswersFromDb : LiveData<List<Source>> get() = _picturesListByAnswersFromDb

    /** Лист озвучек из бд по ответам */
    private var _soundsListByAnswersFromDb = MutableLiveData<List<Source>>()
    val soundsListByAnswersFromDb : LiveData<List<Source>> get() = _soundsListByAnswersFromDb

    /** Озвучка из бд */
    private var _soundFromDb = MutableLiveData<Source>()
    val soundFromDb : LiveData<Source> get() = _soundFromDb

    /** Звуковой эффект из бд для правильного ответа*/
    private var _goodSoundEffect = MutableLiveData<Source>()
    val goodSoundEffect : LiveData<Source> get() = _goodSoundEffect

    /** Звуковой эффект из бд для неправильного ответа */
    private var _badSoundEffect = MutableLiveData<Source>()
    val badSoundEffect : LiveData<Source> get() = _badSoundEffect

    /** Получение озвучек по [soundId] */
    fun getSoundById(soundId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val sound = sourceInteractor.soundSourceById(soundId)
                withContext(Dispatchers.Main) {
                    _soundFromDb.value = sound
                }
            }
        }
    }

    /** Получение звуквого эффекта при правильном ответе */
    fun okEffect() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val rightAnswerEffect = sourceInteractor.rightAnswerSource()
                withContext(Dispatchers.Main) {
                    _goodSoundEffect.value = rightAnswerEffect
                }
            }
        }
    }



    /** Получение звукового эффекта при неправильном ответе */
    fun wrongEffect() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val wrongAnswerEffect = sourceInteractor.wrongAnswerSource()
                withContext(Dispatchers.Main) {
                    _badSoundEffect.value = wrongAnswerEffect
                }
            }
        }
    }

    /** Получение ответа по [taskId] */
    fun getAnswers(taskId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val answers = answersByTaskIdUseCase(taskId)
                withContext(Dispatchers.Main) {
                    _answersListFromDb.value = answers
                }
            }
        }
    }

    fun getComplexAnswersByTaskId(taskId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val answers = answersByTaskIdUseCase(taskId)
                val complexAnswers = getComplexAnswerUseCase(answers)
                    withContext(Dispatchers.Main) {
                    _complexAnswersListFromDb.value = complexAnswers
                }
            }
        }
    }

    /** Получение картинок по списку ответов [answers] */
    fun getPicturesByAnswers(answers : List<Answer>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val pictures = sourceInteractor.picturesByAnswers(answers)
                withContext(Dispatchers.Main) {
                    _picturesListByAnswersFromDb.value = pictures
                }
            }
        }
    }

    /** Чистка кэша Glide */
    fun clearGlideCache() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                sourceInteractor.clearGlideCaches()
            }
        }
    }

    /** Получение озвучек для ответов [answers] */
    fun getSoundsByAnswers(answers: List<Answer>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val sounds = sourceInteractor.soundsByAnswers(answers)
                withContext(Dispatchers.Main) {
                    _soundsListByAnswersFromDb.value = sounds
                }
            }
        }
    }

    /** Получение заданий в очерёдности, заданной в orders */
    fun getTasksFromOrder() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val tasks = tasksByOrdersUseCase(getAllOrdersUseCase())
                withContext(Dispatchers.Main) {
                    _tasksListFromDb.value = tasks
                }
            }
        }
    }
}