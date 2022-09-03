package com.example.adygall2.presentation.view_model

import androidx.lifecycle.*
import com.example.adygall2.data.db_models.*
import com.example.adygall2.data.delegate.AnswerHelper
import com.example.adygall2.domain.usecases.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Вью модель для взаимодействия с данными из бд
 */
class GameViewModel(
    private val answerUseCase: AnswerUseCase,
    private val orderUseCase: OrderUseCase,
    private val pictureUseCase: PictureUseCase,
    private val taskUseCase: TaskUseCase,
    private val soundUseCase: SoundUseCase,
    private val soundEffectUseCase: SoundEffectUseCase,
    private val answerHelperDelegate : AnswerHelper
) : ViewModel(), AnswerHelper by answerHelperDelegate {

    /** Лист заданий из бд */
    private var _tasksListFromDb = MutableLiveData<List<Task>>()
    val tasksListFromDb : LiveData<List<Task>> get() = _tasksListFromDb

    /** Лист ответов из бд */
    private var _answersListFromDb = MutableLiveData<MutableList<Answer>>()
    val answersListFromDb : LiveData<MutableList<Answer>> get() = _answersListFromDb

    /** Лист картинок из бд */
    private var _picturesListByAnswersFromDb = MutableLiveData<List<Picture>>()
    val picturesListByAnswersFromDb : LiveData<List<Picture>> get() = _picturesListByAnswersFromDb

    /** Лист озвучек из бд по ответам */
    private var _soundsListByAnswersFromDb = MutableLiveData<List<Sound>>()
    val soundsListByAnswersFromDb : LiveData<List<Sound>> get() = _soundsListByAnswersFromDb

    /** Озвучка из бд */
    private var _soundFromDb = MutableLiveData<Sound>()
    val soundFromDb : LiveData<Sound> get() = _soundFromDb

    /** Звуковой эффект из бд для правильного ответа*/
    private var _goodSoundEffect = MutableLiveData<SoundEffect>()
    val goodSoundEffect : LiveData<SoundEffect> get() = _goodSoundEffect

    /** Звуковой эффект из бд для неправильного ответа */
    private var _badSoundEffect = MutableLiveData<SoundEffect>()
    val badSoundEffect : LiveData<SoundEffect> get() = _badSoundEffect

    /** Получение озвучек по [soundId] */
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

    /** Получение звуквого эффекта при правильном ответе */
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

    /** Получение звукового эффекта при неправильном ответе */
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

    /** Получение ответа по [taskId] */
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

    /** Получение картинок по списку ответов [answers] */
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

    /** Чистка кэша Glide */
    fun clearGlideCache() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                pictureUseCase.clearCaches()
            }
        }
    }

    /** Получение озвучек для ответов [answers] */
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

    /** Получение заданий в очерёдности, заданной в orders */
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