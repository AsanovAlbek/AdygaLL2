package com.example.adygall2.presentation.view_model

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.viewbinding.ViewBinding
import com.example.adygall2.R
import com.example.adygall2.data.delegate.AnswerFormatter
import com.example.adygall2.data.local.levelsNames
import com.example.adygall2.data.models.ResourceProvider
import com.example.adygall2.data.models.SoundsPlayer
import com.example.adygall2.data.room.userbase.ProgressItem
import com.example.adygall2.domain.model.Task
import com.example.adygall2.domain.model.User
import com.example.adygall2.domain.usecases.AnswersByTaskIdUseCase
import com.example.adygall2.domain.usecases.GetComplexAnswerUseCase
import com.example.adygall2.domain.usecases.SourceInteractor
import com.example.adygall2.domain.usecases.TasksByLessonUseCase
import com.example.adygall2.domain.usecases.UserUseCase
import com.example.adygall2.presentation.activities.UserChangeListener
import com.example.adygall2.presentation.adapters.groupieitems.questions.parentitem.QuestionItem
import com.example.adygall2.presentation.adapters.groupieitems.questions.parentitem.createQuestion
import com.example.adygall2.presentation.fragments.main.FragmentGamePageDirections
import com.example.adygall2.presentation.model.DialogState
import com.example.adygall2.presentation.model.GameState
import com.google.android.material.snackbar.Snackbar
import java.util.Date
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Вью модель для взаимодействия с данными из бд
 */
class GameViewModel(
    private val answersByTaskIdUseCase: AnswersByTaskIdUseCase,
    private val getComplexAnswerUseCase: GetComplexAnswerUseCase,
    private val sourceInteractor: SourceInteractor,
    private val resourceProvider: ResourceProvider,
    private val soundsPlayer: SoundsPlayer,
    private val userUseCase: UserUseCase,
    private val tasksByLessonUseCase: TasksByLessonUseCase,
    private val mainDispatcher: CoroutineDispatcher,
    private val ioDispatcher: CoroutineDispatcher,
    private val answerFormatterDelegate: AnswerFormatter
) : ViewModel(), AnswerFormatter by answerFormatterDelegate {

    companion object {
        private const val fiveMinuteInMills =
            300_000L // 5 минут * 60 секунд * 1000 милисекунд. 5 минут в милисекундах

        /** Максимальное количество ошибок */
        private const val MISTAKES_LIMIT = 30
        private const val HP_DAMAGE = 5
        private const val MONEY_INCREMENT = 1
        private const val TAG = "GameFragment"
    }

    private var isLessonRepeated = false

    /** Данные о пользователе, хранимые в памяти телефона */
    private var _user = User()
    val user get() = _user

    /** Состояние игрового процесса */
    private var _gameState = MutableLiveData(GameState())
    val gameState: LiveData<GameState> get() = _gameState

    /** Вспомогательное свойство для сохранения текущего состояния игрового процесса */
    private var currentGameState = GameState()

    /** Список вопросов */
    private var questionItems = MutableLiveData<MutableList<QuestionItem<out ViewBinding>?>>()
    val items: LiveData<MutableList<QuestionItem<out ViewBinding>?>> get() = questionItems
    private var currentQuestionItems = mutableListOf<QuestionItem<out ViewBinding>?>()

    /** Состояние диалогового окна после ответа на вопрос */
    private var _dialogState = MutableLiveData<DialogState>()
    val dialogState: LiveData<DialogState> get() = _dialogState
    private var currentDialogState = DialogState()

    private fun isLastQuestion() =
        currentGameState.currentQuestionPosition == currentQuestionItems.size - 1

    private fun isNotLastQuestion() =
        currentGameState.currentQuestionPosition < currentQuestionItems.size

    private fun isRight() = transform(currentGameState.userAnswer).compareTo(transform(currentGameState.rightAnswer)) == 0

    /** Получение актуального Question */
    private fun currentQuestion() = currentQuestionItems[currentGameState.currentQuestionPosition]!!
    private fun canSkipCurrentQuestion() = currentQuestion().canSkipQuestion
    private fun onNextQuestion() = currentQuestionItems[currentGameState.currentQuestionPosition]!!.clear()

    /** Получение всех заданий из урока и преобразование в визуальное представление */
    fun start(
        context: Context,
        level: Int,
        lesson: Int,
        levelName: String
    ) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                currentGameState = currentGameState.copy(isLoading = true)
                val gameTasks = tasksByLessonUseCase(level, lesson)
                val questions = gameTasks.map { task ->
                    val answers = getComplexAnswerUseCase(answersByTaskIdUseCase(task.id))
                    task.createQuestion(
                        context = context,
                        title = task.task,
                        answers = answers,
                        soundsPlayer = soundsPlayer,
                        onClearImageCaches = ::clearGlideCache,
                        playerSource = sourceInteractor.soundSourceById(task.soundId)
                    )
                }
                Log.i("corr", questions.joinToString { it?.rightAnswer ?: "NULL" })

                _user = userUseCase.getUser()
                withContext(mainDispatcher) {
                    isLessonRepeated =
                        user.learningProgressSet.contains(ProgressItem(level, lesson))
                    currentQuestionItems = questions.toMutableList()
                    questionItems.value = currentQuestionItems
                    soundsPlayer.setCompletionListener { soundsPlayer.stopPlay() }
                    //getAllNewWords(tasks)
                    currentGameState = currentGameState.copy(
                        startTime = System.currentTimeMillis(),
                        canSkipTask = canSkipCurrentQuestion(),
                        hp = user.hp,
                        coins = user.coins,
                        lessonProgress = lesson,
                        levelProgress = level,
                        currentQuestionPosition = 0,
                        levelName = levelsNames[level - 1],
                        userName = user.name,
                        tasks = gameTasks,
                        isLoading = false
                    )
                    _gameState.value = currentGameState
                    refreshLessonTitle()
                }
            }
        }
    }

    /** Основная функция вычисления ответа пользователя и игрового процесса
     * @param requireView - view для [Snackbar]
     * @param context - context для создания диалоговых окон
     * @param navController - контроллер для навигации между фрагментами
     * @param tasks - задания урока, нужны для вычисления количества выученных за урок слов
     * @param level - уровень урока
     * @param lesson - номер урока
     * @param coinsBeforeLesson - количество монет пользователя в начале урока
     * */
    fun getAnswerButtonClickAction(
        requireView: View,
        context: Context,
        navController: NavController,
        level: Int,
        lesson: Int,
        coinsBeforeLesson: Int,
        coins: Int,
        hp: Int,
        userHealthHandle: UserChangeListener
    ) {
        viewModelScope.launch {
            withContext(mainDispatcher) {
                requireView.clearFocus()
                // Получение правильного ответа и ответа пользователя
                currentQuestion().let { question ->
                    currentGameState = currentGameState.copy(
                        userAnswer = question.userAnswer,
                        rightAnswer = question.rightAnswer,
                        canSkipTask = canSkipCurrentQuestion()
                    )
                    Log.i(
                        "game",
                        "user = ${currentGameState.userAnswer} right = ${currentGameState.rightAnswer}"
                    )
                    _gameState.value = currentGameState
                }

                // Если вопрос не последний
                if (isNotLastQuestion()) {
                    // Если пользователь дал ответ
                    if (currentGameState.userAnswer.isNotEmpty()) {
                        playEffect()
                        showDialog {
                            // Если последний вопрос
                            if (isLastQuestion()) {
                                // Завершаем урок
                                finishLesson(
                                    level = level,
                                    lesson = lesson,
                                    coinsBeforeLesson = coinsBeforeLesson,
                                    navController = navController,
                                    coins = coins,
                                    hp = hp
                                )
                                // Если не последний вопрос
                            } else {
                                //nextTask()
                                // Если ответ не правильный
                                if (!isRight()) {
                                    // Увеличиваем счётчики ошибок
                                    currentGameState = currentGameState.copy(
                                        mistakesCounter = currentGameState.mistakesCounter + 1,
                                        mistakesCount = currentGameState.mistakesCount + 1
                                    )
                                    _gameState.value = currentGameState
                                    // Если счётчик превысил допустимое значение, то прогресс сбрасывается
                                    if (currentGameState.mistakesCounter >= MISTAKES_LIMIT) {
                                        restartLesson()
                                    } else {
                                        // Иначе переход на следующий вопрос
                                        nextTask()
                                    }
                                    // Получение урона за неправильный ответ
                                    damage(userHealthHandle)
                                    // Если при этом закончилось здоровье, то выход из игры
                                    if (currentGameState.hp <= 0) {
                                        fail(
                                            context = context,
                                            navController = navController
                                        )
                                    }
                                } else if (isRight()) {
                                    // Иначе даём монеты и направляем на следующий уровень
                                    giveMoney()
                                    nextTask()
                                }
                            }
                        }

                    } else {
                        // Если пользователь не дал ответ, то показываем сообщение, что надо дать ответ
                        emptyAnswerMessage(requireView)
                    }
                } else {
                    // Если это был последний вопрос
                    // Если ответ не верен
                    if (!isRight()) {
                        // Увеличиваем счётчик ошибок
                        currentGameState = currentGameState.copy(
                            mistakesCount = currentGameState.mistakesCount + 1,
                            mistakesCounter = currentGameState.mistakesCounter + 1
                        )
                        _gameState.value = currentGameState
                        // Если превышен лимит ошибок - сброс прогресса
                        if (currentGameState.mistakesCounter >= MISTAKES_LIMIT) {
                            restartLesson()
                        } else {
                            // Иначе следующий вопрос
                            nextTask()
                        }
                        // Урон
                        damage(userHealthHandle)
                    }
                    // Даём монетки за пройденный урок
                    giveMoney()
                }
            }
        }
    }

    /** Метод вызываемый в случае, когда пользователь израсходует все свои единицы здоровья */
    private fun fail(context: Context, navController: NavController) {
        viewModelScope.launch {
            withContext(mainDispatcher) {
                AlertDialog.Builder(context)
                    .setMessage(resourceProvider.getString(R.string.game_over))
                    .setNeutralButton(resourceProvider.getString(R.string.ok)) { dialog, _ -> dialog.cancel() }
                    .create()
                currentGameState.apply {
                    onNextQuestion()
                    val navigateToHome = FragmentGamePageDirections.actionTaskContainerToHomePage(
                        hp = hp,
                        exp = coins
                    )
                    navController.navigate(navigateToHome)
                }
            }
        }
    }

    /** Метод, вызывающий всплывающее сообщение снизу
     * Уведомляющее пользователя  */
    private fun emptyAnswerMessage(view: View) {
        viewModelScope.launch {
            withContext(mainDispatcher) {
                Snackbar.make(
                    view,
                    resourceProvider.getString(R.string.choose_answer_message),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    /** Метод для выхода из приложения через крестик */
    fun exit(context: Context, navController: NavController) {
        viewModelScope.launch {
            withContext(mainDispatcher) {
                AlertDialog.Builder(context)
                    .setMessage(R.string.exit_from_level_question)
                    .setPositiveButton(R.string.yes) { _, _ ->
                        currentGameState =
                            currentGameState.copy(finishTime = System.currentTimeMillis())
                        _gameState.value = currentGameState
                        currentGameState.run {
                            onNextQuestion()
                            _user = _user.copy(
                                coins = currentGameState.coins,
                                globalPlayingTimeInMillis = user.globalPlayingTimeInMillis + (currentGameState.finishTime - currentGameState.startTime)
                            )
                            viewModelScope.launch {
                                userUseCase.updateUser(user)
                            }
                            val navigateToHome =
                                FragmentGamePageDirections.actionTaskContainerToHomePage(
                                    hp = hp,
                                    exp = coins
                                )
                            navController.navigate(navigateToHome)
                        }
                    }
                    .setNegativeButton(R.string.no) { dialog, _ -> dialog.dismiss() }
                    .show()
            }
        }
    }

    /** Метод для показа диалогового окна после ответа */
    private fun showDialog(dialogButtonClick: () -> Unit) {
        viewModelScope.launch {
            withContext(mainDispatcher) {
                currentDialogState = currentDialogState.copy(
                    rootVisible = true,
                    viewPagerEnabled = false,
                    answerButtonEnabled = false,
                    buttonAction = {
                        dialogButtonClick()
                        currentDialogState = currentDialogState.copy(
                            rootVisible = false,
                            viewPagerEnabled = true,
                            answerButtonEnabled = true,
                        )
                        _dialogState.value = currentDialogState
                    }
                )
                currentGameState = currentGameState.copy(canSkipTask = false)
                _gameState.value = currentGameState
                if (isRight()) {
                    currentDialogState = currentDialogState.copy(
                        iconId = R.drawable.like_icon,
                        accuracy = R.string.you_right,
                        accuracyTextColor = R.color.lime_green,
                        rightTextVisible = false,
                        correctAnswerVisible = false
                    )
                    _dialogState.value = currentDialogState
                } else {
                    currentDialogState = currentDialogState.copy(
                        iconId = R.drawable.cross_icon,
                        accuracy = R.string.you_not_right,
                        accuracyTextColor = R.color.soft_red,
                        rightTextVisible = true,
                        correctAnswerVisible = true,
                        correctAnswer = currentGameState.rightAnswer
                    )
                    _dialogState.value = currentDialogState
                }
            }
        }
    }

    private fun nextQuestionAll() {
        viewModelScope.launch {
            withContext(mainDispatcher) {
                currentQuestionItems.forEach { it?.clear() }
                questionItems.value = currentQuestionItems
            }
        }
    }

    /** Метод для пропуска задания */
    fun skipQuestion(
        level: Int,
        lesson: Int,
        coinsBeforeLesson: Int,
        navController: NavController,
        coins: Int,
        hp: Int,
    ) {
        viewModelScope.launch {
            withContext(mainDispatcher) {
                if (isLastQuestion()) {
                    finishLesson(
                        level = level,
                        lesson = lesson,
                        coinsBeforeLesson = coinsBeforeLesson,
                        navController = navController,
                        coins = coins,
                        hp = hp
                    )
                } else {
                    nextTask()
                }
                soundsPlayer.stopPlay()
            }
        }
    }


    /** Метод для завершения урока, так же предусматривает навигацию к [FragmentGameResult] */
    private fun finishLesson(
        level: Int,
        lesson: Int,
        coinsBeforeLesson: Int,
        navController: NavController,
        hp: Int,
        coins: Int
    ) {
        viewModelScope.launch {
            withContext(mainDispatcher) {
                //userUseCase.addCompletedLesson(level, lesson)
                onNextQuestion()
                currentGameState = currentGameState.copy(
                    finishTime = System.currentTimeMillis(),
                    hp = hp,
                    coins = coins
                )
                _gameState.value = currentGameState
                _user = _user.copy(
                    hp = hp,
                    coins = coins,
                    globalPlayingTimeInMillis = user.globalPlayingTimeInMillis + (currentGameState.finishTime - currentGameState.startTime),
                    learningProgressSet = user.learningProgressSet.apply {
                        add(
                            ProgressItem(
                                level,
                                lesson
                            )
                        )
                    }
                )
                userUseCase.updateUser(user)
                getAllNewWords(currentGameState.tasks)
                currentGameState.apply {
                    val navigateToResults =
                        FragmentGamePageDirections.actionTaskContainerToTaskResults(
                            hp = hp,
                            exp = coins,
                            coins = coins - coinsBeforeLesson,
                            lives = hp,
                            mistakes = mistakesCount,
                            time = gameTime(),
                            learnedWords = newWordsCount
                        )
                    navController.navigate(navigateToResults)
                }
            }
        }
    }

    /** Метод для начала урока заново, если пользователь превысил лимит ошибок */
    private fun restartLesson() {
        viewModelScope.launch {
            withContext(mainDispatcher) {
                nextQuestionAll()
                val questions = currentQuestionItems.toList()
                currentQuestionItems.clear()
                questionItems.value = currentQuestionItems
                currentQuestionItems.addAll(questions)
                questionItems.value = currentQuestionItems
                //onNextQuestion()
                currentGameState = currentGameState.copy(
                    mistakesCounter = 0,
                    currentQuestionPosition = 0,
                    coins = 0,
                    canSkipTask = canSkipCurrentQuestion()
                )
                _gameState.value = currentGameState
                refreshLessonTitle()
            }
        }
    }

    /** Метод перехода к следующему занятию */
    private fun nextTask() {
        viewModelScope.launch {
            withContext(mainDispatcher) {
                onNextQuestion()
                currentGameState = currentGameState.copy(
                    currentQuestionPosition = currentGameState.currentQuestionPosition + 1
                )
                currentGameState = currentGameState.copy(
                    canSkipTask = canSkipCurrentQuestion()
                )
                _gameState.value = currentGameState
            }
            refreshLessonTitle()
        }
    }

    /** Получение урона за неправильный ответ*/
    private fun damage(userHealthHandle: UserChangeListener) {
        viewModelScope.launch {
            withContext(mainDispatcher) {
                userHealthHandle.damage()
            }
        }
    }

    /** Получение монет за правильный ответ */
    private fun giveMoney() {
        viewModelScope.launch {
            withContext(mainDispatcher) {
                if (!isLessonRepeated) {
                    currentGameState =
                        currentGameState.copy(coins = currentGameState.coins + MONEY_INCREMENT)
                    _gameState.value = currentGameState
                }
            }
        }
    }

    /** Вычисление времени урока */
    private fun gameTime(): String = DateFormat.format(
        resourceProvider.getString(R.string.minutes_and_seconds_format),
        currentGameState.finishTime - currentGameState.startTime
    ).toString()

    /** Сохранение статистики пользователя после выхода */
    fun saveUserStates(hp: Int, coins: Int) {
        viewModelScope.launch {
            withContext(mainDispatcher) {
                currentGameState = currentGameState.copy(finishTime = System.currentTimeMillis())
                if (Date().time - _user.lastOnlineTimeInMillis >= 300_000) {
                    _user = _user.copy(
                        lastOnlineTimeInMillis = Date().time
                    )
                }
                _user = _user.copy(
                    hp = hp,
                    coins = coins,
                    globalPlayingTimeInMillis = user.globalPlayingTimeInMillis + (currentGameState.finishTime - currentGameState.startTime)
                )
                Log.i("user", "save and exit game")
                userUseCase.updateUser(user)
            }
        }
    }

    /** Проигрывание аудио эффекта */
    private fun playEffect() {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                val soundEffect = if (isRight()) {
                    sourceInteractor.rightAnswerSource()
                } else {
                    sourceInteractor.wrongAnswerSource()
                }

                withContext(mainDispatcher) {
                    soundsPlayer.stopPlay()
                    soundsPlayer.playSound(soundEffect)
                }
            }
        }
    }

    /** Чистка кэша Glide */
    fun clearGlideCache() {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                sourceInteractor.clearGlideCaches()
            }
        }
    }

    /** Получение всех новых слов и их сохранение в памяти */
    private fun getAllNewWords(tasks: List<Task>) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                val answersAll = tasks.map { task ->
                    answersByTaskIdUseCase(task.id)
                }.flatten().map { answer ->
                    transform(answer.answer).split(" ")
                }.flatten().distinct()

                withContext(mainDispatcher) {
                    val oldWordsCount = _user.learnedWords.size
                    _user.learnedWords.addAll(answersAll)
                    currentGameState = currentGameState.copy(
                        newWordsCount = _user.learnedWords.size - oldWordsCount
                    )
                    _gameState.value = currentGameState
                    Log.i("user", "update words and save")
                    userUseCase.updateUser(user)
                }
            }
        }
    }

    /** Обновление заголовка урока */
    private fun refreshLessonTitle() {
        viewModelScope.launch {
            withContext(mainDispatcher) {
                currentGameState = currentGameState.copy(
                    lessonTitle = "${currentGameState.levelName}. " +
                            "Урок ${currentGameState.lessonProgress}. " +
                            "Задание ${currentGameState.currentQuestionPosition + 1}."
                    /*lessonTitle = resourceProvider.getString(
                        R.string.lesson_title_mask,
                        currentGameState.levelName,
                        currentGameState.lessonProgress,
                        currentGameState.currentQuestionPosition + 1
                    )*/
                )
                _gameState.value = currentGameState
            }
        }
    }

    /** Получение фото пользователя из кэша приложения */
    fun getPhotoFromCache() = userUseCase.getUserImage(resourceProvider)
}