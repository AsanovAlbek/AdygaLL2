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
import com.example.adygall2.data.db_models.SoundEffect
import com.example.adygall2.data.db_models.Task
import com.example.adygall2.data.models.SoundsPlayer
import com.example.adygall2.databinding.TaskContainerBinding
import com.example.adygall2.presentation.GameViewModel
import com.example.adygall2.presentation.adapters.TasksAdapter
import com.example.adygall2.presentation.consts.ArgsKey.MY_LOG_TAG
import com.example.adygall2.presentation.fragments.tasks.FillGapsFragment
import com.example.adygall2.presentation.fragments.tasks.FillPassTask
import com.example.adygall2.presentation.fragments.tasks.FourImageQuestion
import com.example.adygall2.presentation.fragments.tasks.PairsOfWordsFragment
import com.example.adygall2.presentation.fragments.tasks.SentenceBuildQuestion
import com.example.adygall2.presentation.fragments.tasks.ThreeWordsQuestion
import com.example.adygall2.presentation.fragments.tasks.TranslateTheTextTask
import com.example.adygall2.presentation.fragments.tasks.TypeThatHeardTask
import com.google.android.material.snackbar.Snackbar

import org.koin.androidx.viewmodel.ext.android.viewModel

class TaskContainer : Fragment(R.layout.task_container) {

    private lateinit var _binding : TaskContainerBinding
    private val binding get() = _binding
    private val viewModel by viewModel<GameViewModel>()
    private lateinit var soundsPlayer: SoundsPlayer

    /** Счётчик ошибок */
    private var mistakesCounter = 0
    /** Количество монет, получаемое за один правильный ответ
     * Вычисляется по формуле: количество заданий / 100
     * Если откажемся от прогресс бара, то формула будет не нужна*/
    private var moneyIncrementValue = 0f

    companion object {
        /** Максимальное количество ошибок */
        private const val mistakesLimit = 3
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = TaskContainerBinding.inflate(inflater, container, false)

        soundsPlayer = SoundsPlayer(requireContext())
        binding.taskTopBar.LevelNumber.text = "Урок 1"
        getUserStates()
        customizeStepViewBar(14)
        setObservers()
        setListeners()

        return binding.root
    }

    /**
     * Метод получает значения полосок здоровья и опыта от домашней страницы
     */
    private fun getUserStates() {
        binding.taskBottomBar.userHealthBar.progress = requireArguments().getInt("hp")
        binding.taskBottomBar.userExperienceBar.progress = requireArguments().getInt("exp")
    }

    /** Метод для подписки на слушателей из ViewModel */

    private fun setObservers() {
        viewModel.getTasksFromOrder()
        viewModel.tasksListFromDb.observe(viewLifecycleOwner) {
            val tasksSize = viewModel.tasksListFromDb.value?.size
            //customizeStepViewBar(tasksSize!!)
            setViewPager(it)
            moneyIncrementValue = (it.size / 100).toFloat()
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

        binding.getAnswerButton.answerBtn.setOnClickListener {
            val currentFragment = (binding.taskViewPager.adapter as TasksAdapter)
                .getTaskFragment(binding.taskViewPager.currentItem)

            when(currentFragment) {
                is FourImageQuestion -> {
                    userAnswer = currentFragment.userAnswer
                    rightAnswer = currentFragment.rightAnswer
                }

                is SentenceBuildQuestion -> {
                    userAnswer = currentFragment.userAnswer.joinToString { it.answer }
                    rightAnswer = currentFragment.rightAnswer.joinToString()
                }

                is ThreeWordsQuestion -> {
                    userAnswer = currentFragment.userAnswer
                    rightAnswer = currentFragment.rightAnswer
                }

                is PairsOfWordsFragment -> {
                    userAnswer = currentFragment.userAnswerPairsCheckList.joinToString()
                    rightAnswer = currentFragment.rightAnswerPairsCheckList.joinToString()
                }

                is FillGapsFragment -> {
                    userAnswer = currentFragment.userAnswer.joinToString { it.text }
                    rightAnswer = currentFragment.rightAnswer
                }

                is TypeThatHeardTask -> {
                    userAnswer = currentFragment.userAnswer.split(" ", ",").joinToString()
                    rightAnswer = currentFragment.rightAnswer.split(" ", " ").joinToString()
                }

                is TranslateTheTextTask -> {
                    userAnswer = currentFragment.userAnswer.joinToString() { it.answer }
                    rightAnswer = currentFragment.rightAnswer.split(" ", " ").joinToString()
                }

                is FillPassTask -> {
                    userAnswer = currentFragment.userAnswer
                    rightAnswer = currentFragment.rightAnswer
                }
            }

            // Пока не дошли до последнего элемента
            if (binding.taskViewPager.currentItem < binding.taskViewPager.adapter!!.itemCount - 1) {

                // Если ответ выбран и получен
                if (userAnswer != "") {
                    showRightAnswer(userAnswer, rightAnswer)
                    binding.taskViewPager.currentItem += 1

                    // Если ответил неправильно
                    if (userAnswer.compareTo(rightAnswer) != 0) {
                        // Если игрок ошибся 5 раз, то его прогресс аннулируется
                        mistakesCounter++
                        if (mistakesCounter >= mistakesLimit) {
                            restartLesson()
                        }
                        // Если игрок ошибся, то у него отнимается 10 очков здоровья
                        binding.taskBottomBar.userHealthBar.progress -= 10
                        // Если у игрока закончилось здоровье
                        if (binding.taskBottomBar.userHealthBar.progress <= 0) {
                            dialog("Провал")
                            exitIntoLvl()
                        }

                        val badSoundEffect = viewModel.badSoundEffect.value
                        badSoundEffect?.let { soundsPlayer.playSound(it) }

                        // Если ответил правильно
                    } else {
                        binding.taskBottomBar.userExperienceBar.progress += 10
                        val goodSoundEffect = viewModel.goodSoundEffect.value
                        goodSoundEffect?.let { soundsPlayer.playSound(it) }
                    }

                    // Изменение (шаг) шкалы прогресса
                    binding.taskTopBar.levelProgress.apply {
                        completedPosition = binding.taskViewPager.currentItem
                        drawView()
                    }
                }
                else {
                    snackBar("Пожалуйста, выберите ответ")
                }
            }
            // Если это был последний вопрос
            else {
                    showRightAnswer(userAnswer, rightAnswer)
                    dialog("Пройдено")
                    // Пока что возвращается на главный экран
                    // Если пройдено успешно, то +20 очков опыта
                    binding.taskBottomBar.userExperienceBar.progress += moneyIncrementValue.toInt()
                    exitIntoLvl()
                }
            }

        // Присвоение нажатия кнопке выхода
        binding.taskTopBar.closeButton.setOnClickListener {
            // Создание диалога для выхода
            exitIntoLevelDialog()
        }

    }

    /**
     * Метод для перезапуска уровня
     * Используется если пользователь достиг [mistakesLimit]
     */
    private fun restartLesson() {
            binding.taskViewPager.currentItem = 0
            binding.taskBottomBar.userExperienceBar.progress = 0
            mistakesCounter = 0
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
            putInt("hp", binding.taskBottomBar.userHealthBar.progress)
            putInt("exp", binding.taskBottomBar.userExperienceBar.progress)
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
            putInt("hp", binding.taskBottomBar.userHealthBar.progress)
            putInt("exp", binding.taskBottomBar.userExperienceBar.progress)
        }
        findNavController().navigate(R.id.homePage, hpAndExpProgress)
    }

    // Допилю когда буду делать мног флажков и viewpager под них
    private fun setTasks() {
        val taskList = arguments?.getParcelableArray("taskList") as List<Task>

        Log.i(MY_LOG_TAG, taskList.toString())
        val fragmentAdapter = TasksAdapter(requireActivity(), taskList)
        fragmentAdapter.setTaskSkipListener {
            binding.taskViewPager.currentItem += 1
        }
        binding.taskViewPager.adapter = fragmentAdapter
        binding.taskViewPager.isUserInputEnabled = false
    }

    /**
     * Метод для получения заданий из базы
     */
    private fun setViewPager(tasks : List<Task>) {

        val fragmentAdapter = TasksAdapter(requireActivity(), tasks)
        fragmentAdapter.setTaskSkipListener {
            binding.taskViewPager.currentItem += 1
        }
        binding.taskViewPager.adapter = fragmentAdapter
        binding.taskViewPager.isUserInputEnabled = false
    }

    /**
     * Метод для показа правильного ответа и ответа пользователя
     * p.s. чтоб задизайнить окно надо будет заменить AlertDialog на DialogFragment
     */
    private fun showRightAnswer(userAnswer : String, rightAnswer : String) {

        val explanation =
            if (userAnswer.compareTo(rightAnswer) == 0) "Молодец!"
            else "Неверный ответ \n\n Правильный ответ: $rightAnswer " +
                    "\n Ваш ответ: $userAnswer \nВы теряете 10 ед. здоровья!"

        dialog(explanation)
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
            binding.gameTaskContainer,
            message,
            Snackbar.LENGTH_SHORT
        ).setTextColor(Color.GREEN).show()
    }

    /** Метод для отрисовки линии  */
    private fun customizeStepViewBar(taskCount : Int) {
        // массив из элементов, заполненных пустой строкой
        val steps = Array(taskCount) { " " }
            binding.taskTopBar.levelProgress.apply {
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