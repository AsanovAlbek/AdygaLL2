package com.example.adygall2.presentation.fragments.menu

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.adygall2.R
import com.example.adygall2.data.local.PrefConst
import com.example.adygall2.domain.model.Task
import com.example.adygall2.data.models.SoundsPlayer
import com.example.adygall2.databinding.TaskContainerBinding
import com.example.adygall2.domain.model.Source
import com.example.adygall2.presentation.view_model.GameViewModel
import com.example.adygall2.presentation.adapters.TasksAdapter
import com.example.adygall2.presentation.fragments.dialog
import com.example.adygall2.presentation.fragments.snackBar
import java.time.format.DateTimeFormatter
import java.util.Date
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named

/**
 * Фрагмент для игрового процесса
 */
class FragmentGamePage : Fragment(R.layout.task_container) {

    private lateinit var _taskContainerBinding : TaskContainerBinding
    private val taskContainerBinding get() = _taskContainerBinding
    private val viewModel by viewModel<GameViewModel>()
    private val soundsPlayer: SoundsPlayer by inject()
    private val gameArgs: FragmentGamePageArgs by navArgs()
    private lateinit var fragmentAdapter: TasksAdapter

    private val userHpPref: SharedPreferences by inject(named(PrefConst.USER_HP))
    private val expPref: SharedPreferences by inject(named(PrefConst.USER_EXP))
    private val levelProgressPref: SharedPreferences by inject(named(PrefConst.LEVEL_PROGRESS))
    private val lessonProgressPref: SharedPreferences by inject(named(PrefConst.LESSON_PROGRESS))

    /** Время прохождения */
    private var gameStartedTime = 0L
    private var gameFinishedTime = 0L
    /** Счётчик всех ошибок за урок */
    private var totalMistakes = 0
    /** Счётчик ошибок */
    private var mistakesCounter = 0
    /** Количество монет, получаемое за один правильный ответ
     * Вычисляется по формуле: количество заданий / 100
     * Если откажемся от прогресс бара, то формула будет не нужна*/
    private var moneyIncrementValue = 1
        set(value) {
            if (value > 1) {
                field = value
            }
        }

    companion object {
        /** Максимальное количество ошибок */
        private const val MISTAKES_LIMIT = 3

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
        getUserStates()
        handleTasks(gameArgs.tasks.toList())
        taskContainerBinding.taskTopBar.LevelNumber.text = "Урок ${gameArgs.lessonProgress}-1"
        setListeners()

        gameStartedTime = System.currentTimeMillis()

        viewModel.getTasksFromOrder()
        viewModel.okEffect()
        viewModel.wrongEffect()
    }

    override fun onStop() {
        super.onStop()
        userHpPref.edit {
            // Сохраняем здоровье и монеты при выходе
            putInt(PrefConst.USER_HP,taskContainerBinding.taskBottomBar.hp.progress)
        }
        expPref.edit {
            putInt(PrefConst.USER_EXP, taskContainerBinding.taskBottomBar.exp.progress)
        }
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

    private fun handleTasks(tasks: List<Task>) {
        setViewPager(tasks)
        moneyIncrementValue = 100 / tasks.size
    }

    private fun soundEffect(source : Source) {
        soundsPlayer.playSound(source)
        soundsPlayer.setCompletionListener { soundsPlayer.stopPlay() }
    }

    private fun wrongEffect() {
        viewModel.badSoundEffect.observe(viewLifecycleOwner, ::soundEffect)
    }

    private fun goodEffect() {
        viewModel.goodSoundEffect.observe(viewLifecycleOwner, ::soundEffect)
    }

    private fun giveMoney() {
        taskContainerBinding.taskBottomBar.exp.progress += moneyIncrementValue
    }

    private fun takeAwayHp() {
        taskContainerBinding.taskBottomBar.hp.progress -= 10
    }

    private fun goNextTask() {
        taskContainerBinding.taskViewPager.currentItem += 1
    }

    private fun isLastTask() = with(taskContainerBinding.taskViewPager) {
        currentItem == adapter!!.itemCount - 1
    }

    private fun isNotLastTask() = with(taskContainerBinding.taskViewPager) { currentItem < adapter!!.itemCount }

    private fun incrementProgress() {
        taskContainerBinding.apply {
            taskTopBar.progressIndicator.progress = taskViewPager.currentItem * moneyIncrementValue
        }
    }

    /**
     * Метод для присвоения обработчиков нажатий
     */
    private fun setListeners() {
        var userAnswer = ""
        var rightAnswer = ""

        taskContainerBinding.getAnswerButton.answerBtn.setOnClickListener {
            // Получаем фрагмент, который сейчас на экране
            val currentFragment = (taskContainerBinding.taskViewPager.adapter as TasksAdapter)
                .getTaskFragment(taskContainerBinding.taskViewPager.currentItem)

            // Получение ответов
            userAnswer = currentFragment!!.userAnswer
            rightAnswer = currentFragment.rightAnswer

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
                                goNextTask()
                                // Если ответил неправильно
                                if (userAnswer.compareTo(rightAnswer) != 0) {
                                    // Если игрок ошибся 5 раз, то его прогресс аннулируется
                                    mistakesCounter++
                                    totalMistakes++
                                    if (mistakesCounter >= MISTAKES_LIMIT) {
                                        restartLesson()
                                    } else { incrementProgress() }
                                    // Если игрок ошибся, то у него отнимается 10 очков здоровья
                                    takeAwayHp()
                                    // Если у игрока закончилось здоровье, то он выходит из уровня
                                    if (taskContainerBinding.taskBottomBar.hp.progress <= 0) {
                                        dialog("Провал")
                                        exitIntoLvl()
                                    }

                                    // Если ответил правильно
                                } else {
                                    // Увеличиваем монетки
                                    giveMoney()
                                    incrementProgress()
                                }

                                taskTopBar.LevelNumber.text =
                                    "Урок ${gameArgs.lessonProgress}-${taskViewPager.currentItem + 1}"
                            }
                        }
                        // Если дошли до последнего вопроса и нажали, то выходим
                        if (isLastTask()) {
                            exitIntoLvl()
                        }
                }
                else {
                    // Если ответ не выбран, то выводится сообщение
                    snackBar("Пожалуйста, выберите ответ")
                }
            }
            // Если это был последний вопрос
            else {
                    dialog("Пройдено")
                    // Даём ещё монет и выходим из уровня
                    giveMoney()
                    exitIntoLvl()
                }
            }

        // Присвоение нажатия кнопке выхода
        taskContainerBinding.taskTopBar.closeButton.setOnClickListener {
            // Создание диалога для выхода
            exitIntoLevelDialog()
        }
    }

    /**
     * Метод для перезапуска уровня
     * Используется если пользователь достиг [MISTAKES_LIMIT]
     */
    private fun restartLesson() {
        taskContainerBinding.apply {
            // Перебрасываем пользователя на первое задание
            taskViewPager.currentItem = 0
            taskTopBar.progressIndicator.progress = 0
            // Обнуляем набранные монетки
            taskBottomBar.exp.progress = 0
            // Обнуляем счётчик ошибок
            mistakesCounter = 0
            taskTopBar.LevelNumber.text = "Урок ${gameArgs.lessonProgress}-1"
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

        with(taskContainerBinding.taskBottomBar) {
            val coinsDiff = exp.progress - gameArgs.exp
            val actionToResults = FragmentGamePageDirections.actionTaskContainerToTaskResults(
                hp = hp.progress,
                exp = exp.progress,
                coins = coinsDiff,
                lives = hp.progress,
                mistakes = totalMistakes,
                time = gameTime,
                learnedWords = 0
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
                putInt(PrefConst.LESSON_PROGRESS ,gameArgs.lessonProgress)
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

    /**
     * Метод для получения заданий из базы
     */
    private fun setViewPager(tasks : List<Task>) {

        fragmentAdapter = TasksAdapter(requireActivity(), tasks)
        with(taskContainerBinding) {
            fragmentAdapter.setTaskSkipListener {
                goNextTask()
                incrementProgress()
                taskTopBar.LevelNumber.text = "Урок ${gameArgs.lessonProgress}-${taskViewPager.currentItem + 1}"
            }
            taskViewPager.adapter = fragmentAdapter
            taskViewPager.isUserInputEnabled = false
        }
    }

    private fun showAnswerResultDialog(userAnswer : String, rightAnswer : String, listener: (() -> Unit)) {
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

    /**
     * При скрытии фрагмента чистится кэш картинок
     */
    override fun onPause() {
        super.onPause()
        viewModel.clearGlideCache()
        fragmentAdapter.onFinish()
    }
}