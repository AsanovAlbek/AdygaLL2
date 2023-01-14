package com.example.adygall2.presentation.view_model

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.viewbinding.ViewBinding
import com.example.adygall2.data.delegate.AnswerFormatter
import com.example.adygall2.data.models.SoundsPlayer
import com.example.adygall2.domain.model.Source
import com.example.adygall2.domain.model.Task
import com.example.adygall2.domain.usecases.AnswersByTaskIdUseCase
import com.example.adygall2.domain.usecases.GetAllOrdersUseCase
import com.example.adygall2.domain.usecases.GetComplexAnswerUseCase
import com.example.adygall2.domain.usecases.SourceInteractor
import com.example.adygall2.domain.usecases.TasksByOrdersUseCase
import com.example.adygall2.presentation.adapters.groupieitems.questions.parentitem.QuestionItem
import com.example.adygall2.presentation.adapters.groupieitems.questions.parentitem.createQuestion
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

    private var question = MutableLiveData<QuestionItem<out ViewBinding>>()
    val item: LiveData<QuestionItem<out ViewBinding>> get() = question

    private var questionItems = MutableLiveData<MutableList<QuestionItem<out ViewBinding>?>>()
    val items: LiveData<MutableList<QuestionItem<out ViewBinding>?>> get() = questionItems

    private var answersInLesson = MutableLiveData<List<String>>()
    val wordsForStatic: LiveData<List<String>> get() = answersInLesson

    /** Лист заданий из бд */
    private var _tasksListFromDb = MutableLiveData<List<Task>>()
    val tasksListFromDb : LiveData<List<Task>> get() = _tasksListFromDb

    /** Звуковой эффект из бд для правильного ответа*/
    private var _goodSoundEffect = MutableLiveData<Source>()
    val goodSoundEffect : LiveData<Source> get() = _goodSoundEffect

    /** Звуковой эффект из бд для неправильного ответа */
    private var _badSoundEffect = MutableLiveData<Source>()
    val badSoundEffect : LiveData<Source> get() = _badSoundEffect

    fun fillItems(
        context: Context,
        tasks: List<Task>,
        soundsPlayer: SoundsPlayer
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val questions = tasks.map { task ->
                    task.createQuestion(
                        context = context,
                        title = task.task,
                        answers = getComplexAnswerUseCase(answersByTaskIdUseCase(task.id)),
                        soundsPlayer = soundsPlayer,
                        onClearImageCaches = ::clearGlideCache,
                        playerSource = sourceInteractor.soundSourceById(task.soundId)
                    )
                }
                withContext(Dispatchers.Main) {
                    questionItems.value = questions.toMutableList()
                }
            }
        }
    }

    fun makeQuestion(
        context: Context,
        task: Task,
        soundsPlayer: SoundsPlayer
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val newQuestion = task.createQuestion(
                    context = context,
                    title = task.task,
                    answers = getComplexAnswerUseCase(answersByTaskIdUseCase(task.id)),
                    soundsPlayer = soundsPlayer,
                    onClearImageCaches = ::clearGlideCache,
                    playerSource = sourceInteractor.soundSourceById(task.soundId)
                )
                withContext(Dispatchers.Main) {
                    newQuestion?.let {
                        Log.i("gvm", "Новое значение вопроса id = ${task.id}")
                        question.value = it
                    }
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

    /** Чистка кэша Glide */
    fun clearGlideCache() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                sourceInteractor.clearGlideCaches()
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

    fun getAllNewWords(tasks: List<Task>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val answersAll = tasks.map { task ->
                    answersByTaskIdUseCase(task.id)
                }.flatten().map { answer ->
                    transform(answer.answer).split(", ")
                }.flatten().distinct()

                withContext(Dispatchers.Main) {
                    Log.i("tt", "answersAll = ${answersAll.joinToString()}")
                    answersInLesson.value = answersAll
                }
            }
        }
    }
}