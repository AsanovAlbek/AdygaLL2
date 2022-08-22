package com.example.adygall2.presentation.fragments.menu

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.adygall2.R
import com.example.adygall2.data.db_models.Task
import com.example.adygall2.data.models.SoundsPlayer
import com.example.adygall2.databinding.TaskContainerBinding
import com.example.adygall2.presentation.view_model.GameViewModel
import com.example.adygall2.presentation.adapters.TasksAdapter
import com.example.adygall2.presentation.fragments.tasks.FillGapsFragment
import com.example.adygall2.presentation.fragments.tasks.FillPassTask
import com.example.adygall2.presentation.fragments.tasks.FourImageQuestion
import com.example.adygall2.presentation.fragments.tasks.PairsOfWordsFragment
import com.example.adygall2.presentation.fragments.tasks.SentenceBuildQuestion
import com.example.adygall2.presentation.fragments.tasks.ThreeWordsQuestion
import com.example.adygall2.presentation.fragments.tasks.TranslateTheTextTask
import com.example.adygall2.presentation.fragments.tasks.TypeThatHeardTask
import com.example.adygall2.presentation.fragments.tasks.TypeTranslateTask
import com.google.android.material.snackbar.Snackbar

import org.koin.androidx.viewmodel.ext.android.viewModel

class TaskContainer : Fragment(R.layout.task_container) {

    private lateinit var _taskContainerBinding : TaskContainerBinding
    private val taskContainerBinding get() = _taskContainerBinding
    private val viewModel by viewModel<GameViewModel>()
    private lateinit var soundsPlayer: SoundsPlayer

    /** Счётчик ошибок */
    private var mistakesCounter = 0
    /** Количество монет, получаемое за один правильный ответ
     * Вычисляется по формуле: количество заданий / 100
     * Если откажемся от прогресс бара, то формула будет не нужна*/
    private var moneyIncrementValue = 0
    private var clickCounter = 0

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

        soundsPlayer = SoundsPlayer(requireContext())
        taskContainerBinding.taskTopBar.LevelNumber.text = "Урок 1-1"
        customizeStepViewBar(20)
        setObservers()
        getUserStates()
        setListeners()
        hideExplanations()

        return taskContainerBinding.root
    }

    /**
     * Метод получает значения полосок здоровья и опыта от домашней страницы
     */
    private fun getUserStates() {
        taskContainerBinding.taskBottomBar.userHealthBar.progress = requireArguments().getInt("hp")
        taskContainerBinding.taskBottomBar.userExperienceBar.progress = requireArguments().getInt("exp")
    }

    /** Метод для подписки на слушателей из ViewModel */

    private fun setObservers() {
        viewModel.getTasksFromOrder()
        viewModel.tasksListFromDb.observe(viewLifecycleOwner) {
            Log.i("fff", "it size = ${it.size}")
            setViewPager(it)
            moneyIncrementValue = 100 / it.size
        }
        viewModel.okEffect()
        viewModel.wrongEffect()
    }

    /**
     * Метод для присвоения обработчиков нажатий
     */
    private fun setListeners() {
        var userAnswer = ""
        var rightAnswer = ""

        taskContainerBinding.getAnswerButton.answerBtn.setOnClickListener {
            clickCounter++
            val currentFragment = (taskContainerBinding.taskViewPager.adapter as TasksAdapter)
                .getTaskFragment(taskContainerBinding.taskViewPager.currentItem)

            when(currentFragment) {
                is FourImageQuestion -> {
                    userAnswer = currentFragment.userAnswer
                    rightAnswer = currentFragment.rightAnswer
                }

                is SentenceBuildQuestion -> {
                    userAnswer = currentFragment.userAnswer
                    rightAnswer = currentFragment.rightAnswer
                }

                is ThreeWordsQuestion -> {
                    userAnswer = currentFragment.userAnswer
                    rightAnswer = currentFragment.rightAnswer
                }

                is PairsOfWordsFragment -> {
                    userAnswer = currentFragment.userAnswer
                    rightAnswer = currentFragment.rightAnswer
                }

                is FillGapsFragment -> {
                    userAnswer = currentFragment.userAnswer
                    rightAnswer = currentFragment.rightAnswer
                }

                is TypeThatHeardTask -> {
                    userAnswer = currentFragment.userAnswer
                    rightAnswer = currentFragment.rightAnswer
                }

                is TranslateTheTextTask -> {
                    userAnswer = currentFragment.userAnswer
                    rightAnswer = currentFragment.rightAnswer
                }

                is FillPassTask -> {
                    userAnswer = currentFragment.userAnswer
                    rightAnswer = currentFragment.rightAnswer
                }

                is TypeTranslateTask -> {
                    userAnswer = currentFragment.userAnswer
                    rightAnswer = currentFragment.rightAnswer
                }
            }

            // Пока не дошли до последнего элемента
            if (taskContainerBinding.taskViewPager.currentItem < taskContainerBinding.taskViewPager.adapter!!.itemCount) {
                // Если ответ выбран и получен
                if (userAnswer != "") {
                    // Каждое второе нажатие
                    if (clickCounter % 2 == 0) {
                        hideExplanations()
                        val btnDefaultText = resources.getText(R.string.get_answer)
                        taskContainerBinding.getAnswerButton.answerBtn.text = btnDefaultText
                        taskContainerBinding.taskViewPager.currentItem += 1
                        taskContainerBinding.taskTopBar.LevelNumber.text = buildString {
                            append("Урок 1-")
                            append(taskContainerBinding.taskViewPager.currentItem + 1)
                        }
                        if (taskContainerBinding.taskViewPager.currentItem == taskContainerBinding.taskViewPager.adapter!!.itemCount - 1) {
                            exitIntoLvl()
                        }
                    }
                    // Каждый первый клик
                    else {
                        showRightAnswer(userAnswer, rightAnswer)
                        // Если ответил неправильно
                        if (userAnswer.compareTo(rightAnswer) != 0) {
                            // Если игрок ошибся 5 раз, то его прогресс аннулируется
                            mistakesCounter++
                            if (mistakesCounter >= MISTAKES_LIMIT) {
                                restartLesson()
                            }
                            // Если игрок ошибся, то у него отнимается 10 очков здоровья
                            taskContainerBinding.taskBottomBar.userHealthBar.progress -= 10
                            // Если у игрока закончилось здоровье
                            if (taskContainerBinding.taskBottomBar.userHealthBar.progress <= 0) {
                                dialog("Провал")
                                exitIntoLvl()
                            }

                            val badSoundEffect = viewModel.badSoundEffect.value
                            badSoundEffect?.let { soundsPlayer.playSound(it) }

                            // Если ответил правильно
                        } else {
                            taskContainerBinding.taskBottomBar.userExperienceBar.progress += 10
                            val goodSoundEffect = viewModel.goodSoundEffect.value
                            goodSoundEffect?.let { soundsPlayer.playSound(it) }
                        }
                    }

                    // Изменение (шаг) шкалы прогресса
                    taskContainerBinding.taskTopBar.levelProgress.apply {
                        completedPosition = taskContainerBinding.taskViewPager.currentItem
                        drawView()
                    }
                }
                else {
                    snackBar("Пожалуйста, выберите ответ")
                }
            }
            // Если это был последний вопрос
            else {
                    dialog("Пройдено")
                    // Пока что возвращается на главный экран
                    // Если пройдено успешно, то +20 очков опыта
                    taskContainerBinding.taskBottomBar.userExperienceBar.progress += moneyIncrementValue.toInt()
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
            taskContainerBinding.taskViewPager.currentItem = 0
            taskContainerBinding.taskBottomBar.userExperienceBar.progress = 0
            mistakesCounter = 0
            hideExplanations()
            clickCounter = 0
            val btnDefaultText = resources.getText(R.string.get_answer)
            taskContainerBinding.getAnswerButton.answerBtn.text = btnDefaultText
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
        val hpAndExpProgress = Bundle()
        hpAndExpProgress.apply {
            putInt("hp", taskContainerBinding.taskBottomBar.userHealthBar.progress)
            putInt("exp", taskContainerBinding.taskBottomBar.userExperienceBar.progress)
        }
        findNavController().navigate(R.id.action_taskContainer_to_taskResults, hpAndExpProgress)
    }

    /**
     * Метод, который выходит на главный экран
     * Передавая ему данные о здоровье и опыте
     */
    private fun exitToHomePage() {
        val hpAndExpProgress = Bundle()
        hpAndExpProgress.apply {
            putInt("hp", taskContainerBinding.taskBottomBar.userHealthBar.progress)
            putInt("exp", taskContainerBinding.taskBottomBar.userExperienceBar.progress)
        }
        findNavController().navigate(R.id.homePage, hpAndExpProgress)
    }

    /**
     * Метод для получения заданий из базы
     */
    private fun setViewPager(tasks : List<Task>) {

        val fragmentAdapter = TasksAdapter(requireActivity(), tasks)
        fragmentAdapter.setTaskSkipListener {
            taskContainerBinding.taskViewPager.currentItem += 1
        }
        taskContainerBinding.taskViewPager.adapter = fragmentAdapter
        taskContainerBinding.taskViewPager.isUserInputEnabled = false
    }

    /**
     * Метод для показа правильного ответа и ответа пользователя
     */
    private fun showRightAnswer(userAnswer : String, rightAnswer : String) {

        if (userAnswer.compareTo(rightAnswer) == 0) {
            taskContainerBinding.correctAnswerWindow.root.visibility = View.VISIBLE
        }
        else {
            taskContainerBinding.incorrectAnswerWindow.root.visibility = View.VISIBLE
            taskContainerBinding.incorrectAnswerWindow.answerExplanation.text = rightAnswer
        }
        val buttonNextText = resources.getText(R.string.next_text).toString()
        taskContainerBinding.getAnswerButton.answerBtn.text = buttonNextText
    }

    /**
     * Метод делает невидимыми окна с ответами
     */
    private fun hideExplanations() {
        taskContainerBinding.incorrectAnswerWindow.root.visibility = View.GONE
        taskContainerBinding.correctAnswerWindow.root.visibility = View.GONE
    }

    /**
     * Метод для вызова диалога с сообщением, в будущем заменить на
     * задизайненный DialogFragment
     */
    private fun dialog(message: String) {
        val alertDialog = AlertDialog.Builder(requireActivity())
        alertDialog
            .setMessage(message)
            .create()
            .show()
    }

    /**
     * Метод для вызова SnackBar с сообщением
     */
    private fun snackBar(message : String) {
        Snackbar.make(
            taskContainerBinding.gameTaskContainer,
            message,
            Snackbar.LENGTH_SHORT
        ).setTextColor(Color.GREEN).show()
    }

    /** Метод для отрисовки линии  */
    private fun customizeStepViewBar(taskCount : Int) {
        Log.i("fff", "step view bar customized")
        // массив из элементов, заполненных пустой строкой
        val steps = Array(taskCount) { " " }
            taskContainerBinding.taskTopBar.levelProgress.apply {
                // Количество делений и их текст (текст нам на них не нужен в данном случае)
                labels = steps
                // Определение цветов шкалы
                barColorIndicator = resources.getColor(R.color.white, requireActivity().theme)
                progressColorIndicator = resources.getColor(R.color.purple, requireActivity().theme)
                labelColorIndicator = resources.getColor(R.color.purple, requireActivity().theme)
                // Начальная позиция
                completedPosition = 0
                drawView()
            }
        }

}