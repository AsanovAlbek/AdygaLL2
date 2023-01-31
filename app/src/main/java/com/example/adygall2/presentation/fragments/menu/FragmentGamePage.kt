package com.example.adygall2.presentation.fragments.menu

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import com.example.adygall2.R
import com.example.adygall2.data.local.PrefConst
import com.example.adygall2.data.models.SoundsPlayer
import com.example.adygall2.databinding.TaskContainerBinding
import com.example.adygall2.domain.model.Source
import com.example.adygall2.domain.model.Task
import com.example.adygall2.presentation.view_model.GameViewModel
import com.example.adygall2.presentation.adapters.groupieitems.questions.parentitem.QuestionItem
import com.example.adygall2.presentation.fragments.dialog
import com.example.adygall2.presentation.fragments.log
import com.example.adygall2.presentation.fragments.snackBar
import com.xwray.groupie.GroupieAdapter
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named

/**
 * Фрагмент для игрового процесса
 */
class FragmentGamePage : Fragment(R.layout.task_container) {

    private lateinit var _taskContainerBinding: TaskContainerBinding
    private val taskContainerBinding get() = _taskContainerBinding
    private val viewModel by viewModel<GameViewModel>()
    private val soundsPlayer: SoundsPlayer by inject()
    private val gameArgs: FragmentGamePageArgs by navArgs()
    private lateinit var tasksAdapter: GroupieAdapter

    private val userHpPref: SharedPreferences by inject(named(PrefConst.USER_HP))
    private val expPref: SharedPreferences by inject(named(PrefConst.USER_EXP))
    private val levelProgressPref: SharedPreferences by inject(named(PrefConst.LEVEL_PROGRESS))
    private val lessonProgressPref: SharedPreferences by inject(named(PrefConst.LESSON_PROGRESS))
    private val learnedWordsPref: SharedPreferences by inject(named(PrefConst.LEARNED_WORDS))

    private var userAnswer = ""
    private var rightAnswer = ""

    /** Время прохождения */
    private var gameStartedTime = 0L
    private var gameFinishedTime = 0L

    /** Счётчик всех ошибок за урок */
    private var totalMistakes = 0

    /** Счётчик ошибок */
    private var mistakesCounter = 0
    private var newWordsCount = 0

    private var currentQuestionPosition = 0
    private var wordsSet = mutableSetOf<String>()

    companion object {
        /** Максимальное количество ошибок */
        private const val MISTAKES_LIMIT = 3
        private const val HP_DAMAGE = 5
        private const val MONEY_INCREMENT = 1
        private const val TAG = "GameFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _taskContainerBinding = TaskContainerBinding.inflate(inflater, container, false)
        return taskContainerBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Присвоение нажатия кнопке выхода
        taskContainerBinding.taskTopBar.closeButton.setOnClickListener {
            // Создание диалога для выхода
            taskContainerBinding.taskBottomBar.exp.progress = expPref.getInt(PrefConst.USER_EXP, 0)
            exitIntoLevelDialog()

        }

        getUserStates()
        tasksAdapter = GroupieAdapter()
        startGame()
        observe()

        taskContainerBinding.taskViewPager.apply {
            adapter = tasksAdapter
            isUserInputEnabled = false
        }

        refreshLessonTitle()
        viewModel.getAllNewWords(gameArgs.tasks.toList())
        viewModel.initAutoHill(taskContainerBinding.taskBottomBar.hp.progress)
        hillHp()
    }

    /**
     * При скрытии фрагмента чистится кэш картинок
     */
    override fun onPause() {
        super.onPause()
        viewModel.clearGlideCache()
    }

    override fun onStop() {
        super.onStop()
        userHpPref.edit {
            // Сохраняем здоровье и монеты при выходе
            putInt(PrefConst.USER_HP, taskContainerBinding.taskBottomBar.hp.progress)
        }
        expPref.edit {
            putInt(PrefConst.USER_EXP, taskContainerBinding.taskBottomBar.exp.progress)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tasksAdapter.clear()
    }

    private fun startGame() {
        gameStartedTime = System.currentTimeMillis()
        viewModel.apply {
            okEffect()
            wrongEffect()
            fillItems(
                context = requireContext(),
                tasks = gameArgs.tasks.toList(),
                soundsPlayer = soundsPlayer
            )
        }
        soundsPlayer.setCompletionListener { soundsPlayer.stopPlay() }
    }

    private fun hillHp() {
        if (taskContainerBinding.taskBottomBar.hp.progress < 100) {
            viewModel.viewModelScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.autoHillHp()
                    viewModel.hpHill.collect {
                        taskContainerBinding.taskBottomBar.hp.progress = it
                    }
                }
            }
        }
    }

    private fun observe() {
        viewModel.wordsForStatic.observe(viewLifecycleOwner, ::calculateNewLearnedWords)
        viewModel.items.observe(viewLifecycleOwner, ::observeItems)
    }

    private fun calculateNewLearnedWords(words: List<String>) {
        wordsSet =
            learnedWordsPref.getStringSet(PrefConst.LEARNED_WORDS, mutableSetOf())?.toMutableSet()
                ?: mutableSetOf()
        wordsSet.apply {
            val oldWordsLength = size
            wordsSet.addAll(words)
            newWordsCount = size - oldWordsLength
        }
    }

    private fun observeItems(questionItems: MutableList<QuestionItem<out ViewBinding>?>) {
        tasksAdapter.update(questionItems)
        setListeners()
        val firstTask = tasksAdapter.getItem(0) as QuestionItem<out ViewBinding>
        taskContainerBinding.soundTaskSkip.isVisible = firstTask.canSkipQuestion
    }

    /**
     * Метод получает значения полосок здоровья и опыта от домашней страницы
     */
    private fun getUserStates() {
        taskContainerBinding.taskBottomBar.apply {
            hp.progress = gameArgs.hp
            exp.progress = gameArgs.exp
        }
    }

    private fun soundEffect(source: Source) {
        soundsPlayer.apply {
            stopPlay()
            playSound(source)
        }
    }

    private fun wrongEffect() {
        viewModel.badSoundEffect.observe(viewLifecycleOwner, ::soundEffect)
    }

    private fun goodEffect() {
        viewModel.goodSoundEffect.observe(viewLifecycleOwner, ::soundEffect)
    }

    private fun giveMoney() {
        taskContainerBinding.taskBottomBar.exp.progress += MONEY_INCREMENT
    }

    private fun takeAwayHp() {
        taskContainerBinding.taskBottomBar.hp.progress -= HP_DAMAGE
    }

    private fun goNextTask() {
        currentQuestionPosition++
        taskContainerBinding.taskViewPager.currentItem = currentQuestionPosition
        log("current item pos after click = $currentQuestionPosition")
        val questionItem =
            tasksAdapter.getItem(currentQuestionPosition) as QuestionItem<out ViewBinding>
        taskContainerBinding.soundTaskSkip.isVisible = questionItem.canSkipQuestion
    }

    private fun isLastTask() = with(taskContainerBinding.taskViewPager) {
        currentQuestionPosition == gameArgs.tasks.size - 1
    }

    private fun isNotLastTask() =
        with(taskContainerBinding.taskViewPager) { currentQuestionPosition < gameArgs.tasks.size }

    private fun incrementProgress() {
        taskContainerBinding.apply {
            taskTopBar.progressIndicator.progress++
        }
    }

    /**
     * Метод для присвоения обработчиков нажатий
     */
    private fun setListeners() {
        taskContainerBinding.getAnswerButton.answerBtn.setOnClickListener {
            log("current item pos = $currentQuestionPosition")
            val question =
                tasksAdapter.getItem(currentQuestionPosition) as QuestionItem<out ViewBinding>

            taskContainerBinding.soundTaskSkip.isVisible = question.canSkipQuestion
            // Получение ответов
            userAnswer = viewModel.transform(question.userAnswer)
            rightAnswer = viewModel.transform(question.rightAnswer)
            log("user = $userAnswer, right = $rightAnswer, mis = $mistakesCounter")

            // Пока не дошли до последнего элемента
            if (isNotLastTask()) {
                // Если ответ выбран и получен
                if (userAnswer.isNotEmpty()) {
                    taskContainerBinding.apply {
                        if (userAnswer.compareTo(rightAnswer) == 0) {
                            goodEffect()
                        } else {
                            wrongEffect()
                        }
                        showAnswerResultDialog(userAnswer, rightAnswer) {
                            if (isLastTask()) {
                                exitIntoLvl()
                            } else {
                                goNextTask()
                                // Если ответил неправильно
                                if (userAnswer.compareTo(rightAnswer) != 0) {
                                    mistakesCounter++
                                    totalMistakes++
                                    // Если игрок ошибся 3 раза, то его прогресс аннулируется
                                    if (mistakesCounter >= MISTAKES_LIMIT) {
                                        restartLesson()
                                        question.onNextQuestion()
                                    } else {
                                        incrementProgress()
                                    }
                                    // Если игрок ошибся, то у него отнимается 10 очков здоровья
                                    takeAwayHp()
                                    // Если у игрока закончилось здоровье, то он выходит из уровня
                                    if (taskContainerBinding.taskBottomBar.hp.progress <= 0) {
                                        dialog("Провал")
                                        exitToHomePage()
                                    }

                                    // Если ответил правильно
                                } else {
                                    // Увеличиваем монетки
                                    giveMoney()
                                    incrementProgress()
                                }
                                question.onNextQuestion()

                                refreshLessonTitle()
                            }
                        }

                    }
                } else {
                    // Если ответ не выбран, то выводится сообщение
                    snackBar("Пожалуйста, выберите ответ")
                }
            }
            // Если это был последний вопрос
            else {
                if (userAnswer.compareTo(rightAnswer) != 0) {
                    mistakesCounter++
                    totalMistakes++
                    if (mistakesCounter >= MISTAKES_LIMIT) {
                        restartLesson()
                        question.onNextQuestion()
                    } else {
                        incrementProgress()
                    }
                    // Если игрок ошибся, то у него отнимается 10 очков здоровья
                    takeAwayHp()
                }
                giveMoney()
            }
        }

        taskContainerBinding.soundTaskSkip.apply {
            log("current item pos = $currentQuestionPosition")
            val question =
                tasksAdapter.getItem(currentQuestionPosition) as QuestionItem<out ViewBinding>

            //isVisible = question.canSkipQuestion
            setOnClickListener {
                goNextTask()
                question.onNextQuestion()
                taskContainerBinding.dialogShowAnswer.root.isVisible = false
                refreshLessonTitle()
                incrementProgress()
                soundsPlayer.stopPlay()
            }
        }
    }

    /**
     * Метод для перезапуска уровня
     * Используется если пользователь достиг [MISTAKES_LIMIT]
     */
    private fun restartLesson() {
        //tasksAdapter.clear()
        taskContainerBinding.apply {
            // Перебрасываем пользователя на первое задание
            currentQuestionPosition = 0
            taskViewPager.currentItem = 0
            taskTopBar.progressIndicator.progress = 0
            // Обнуляем набранные монетки
            taskBottomBar.exp.progress = expPref.getInt(PrefConst.USER_EXP, 0)
            // Обнуляем счётчик ошибок
            mistakesCounter = 0
            //taskTopBar.LevelNumber.text = "Урок ${gameArgs.lessonProgress}-1"
            refreshLessonTitle()
        }
    }

    /** Метод для показа диалогового окна для выхода */
    private fun exitIntoLevelDialog() {
        AlertDialog.Builder(requireActivity()).apply {
            setMessage("Вы точно хотите выйти?")
            setPositiveButton("Да")
            // Обработчик нажатия на кнопку
            { _, _ ->
                // Закрытие диалога и выход из урока
                exitToHomePage()
            }
            setNegativeButton("Нет") { dialog, _ ->
                // Отказ от выхода - закрытие диалогового окна
                dialog.dismiss()
            }
            show()
        }
    }

    private fun refreshLessonTitle() {
        taskContainerBinding.taskTopBar.LevelNumber.text =
            getString(
                R.string.lesson_title_mask,
                gameArgs.lessonProgress,
                taskContainerBinding.taskViewPager.currentItem + 1
            )
    }

    /**
     * Метод, который переходит к экрану с результатами
     * Так же передавая экрану с результатами информацию о количестве здоровья и опыта
     */
    private fun exitIntoLvl() {
        refreshUserProgress()
        gameFinishedTime = System.currentTimeMillis()
        val gameTime = DateFormat.format(
            getString(R.string.minutes_and_seconds_format),
            gameFinishedTime - gameStartedTime
        ).toString()

        learnedWordsPref.edit {
            putStringSet(PrefConst.LEARNED_WORDS, wordsSet)
        }

        with(taskContainerBinding.taskBottomBar) {

            val coinsDiff = exp.progress - gameArgs.exp
            val actionToResults = FragmentGamePageDirections.actionTaskContainerToTaskResults(
                hp = hp.progress,
                exp = exp.progress,
                coins = coinsDiff,
                lives = hp.progress,
                mistakes = totalMistakes,
                time = gameTime,
                learnedWords = newWordsCount
            )
            findNavController().navigate(actionToResults)
        }
    }

    private fun refreshUserProgress() {
        val lesson = lessonProgressPref.getInt(PrefConst.LESSON_PROGRESS, -1)
        val lvl = levelProgressPref.getInt(PrefConst.LEVEL_PROGRESS, -1)

        // Если урок новый (его порядковый номер больше и уровень открыт)
        if (gameArgs.lessonProgress >= lesson && gameArgs.levelProgress >= lvl) {
            lessonProgressPref.edit {
                putInt(PrefConst.LESSON_PROGRESS, gameArgs.lessonProgress)
            }
        }

        if (gameArgs.levelProgress >= lvl) {
            levelProgressPref.edit {
                putInt(PrefConst.LEVEL_PROGRESS, gameArgs.levelProgress)
            }
        }
    }

    /**
     * Метод, который выходит на главный экран
     * Передавая ему данные о здоровье и опыте
     */
    private fun exitToHomePage() {
        with(taskContainerBinding.taskBottomBar) {
            val actionToHomePage = FragmentGamePageDirections.actionTaskContainerToHomePage(
                hp = hp.progress,
                exp = exp.progress
            )
            findNavController().navigate(actionToHomePage)
        }
    }

    private fun showAnswerResultDialog(
        userAnswer: String,
        rightAnswer: String,
        listener: (() -> Unit)
    ) {
        taskContainerBinding.soundTaskSkip.isVisible = false
        val likeIcon = requireContext().resources.getDrawable(R.drawable.like_icon, null)
        val crossIcon = requireContext().resources.getDrawable(R.drawable.cross_icon, null)
        val green = requireContext().resources.getColor(R.color.lime_green, null)
        val red = requireContext().resources.getColor(R.color.soft_red, null)
        val youRightText = requireContext().resources.getString(R.string.you_right)
        val youNotRightText = requireContext().resources.getString(R.string.you_not_right)

        taskContainerBinding.dialogShowAnswer.apply {
            root.isVisible = true
            taskContainerBinding.taskViewPager.isEnabled = false
            taskContainerBinding.getAnswerButton.root.isEnabled = false
            nextButton.setOnClickListener {
                listener.invoke()
                root.isVisible = false
                taskContainerBinding.taskViewPager.isEnabled = true
                taskContainerBinding.getAnswerButton.root.isEnabled = true
            }
            if (userAnswer.compareTo(rightAnswer) == 0) {
                icon.setImageDrawable(likeIcon)
                accuracy.text = youRightText
                accuracy.setTextColor(green)
                rightTextIs.isVisible = false
                correctAnswer.isVisible = false
            } else {
                icon.setImageDrawable(crossIcon)
                accuracy.text = youNotRightText
                accuracy.setTextColor(red)
                rightTextIs.isVisible = true
                correctAnswer.isVisible = true
                correctAnswer.text = rightAnswer
            }
        }
    }
}