package com.example.adygall2.presentation.fragments

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
import com.example.adygall2.data.db_models.Answer
import com.example.adygall2.data.db_models.Task
import com.example.adygall2.databinding.TaskContainerBinding
import com.example.adygall2.presentation.GameViewModel
import com.example.adygall2.presentation.adapters.TasksAdapter
import com.example.adygall2.presentation.consts.ArgsKey.MY_LOG_TAG
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class TaskContainer : Fragment(R.layout.task_container) {

    private lateinit var _binding : TaskContainerBinding
    private val binding get() = _binding
    private val viewModel by viewModel<GameViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = TaskContainerBinding.inflate(inflater, container, false)

        getUserStates()
        customizeStepViewBar(18)
        setViewModelObservers()
        setListeners()

        return binding.root
    }

    private fun setViewModelObservers() {
        viewModel.getTasksFromOrder()
        viewModel.tasksListFromDb.observe(viewLifecycleOwner, ::setTasks)
    }

    /**
     * Метод получает значения полосок здоровья и опыта от домашней страницы
     */
    private fun getUserStates() {
        binding.taskBottomBar.userHealthBar.progress = arguments!!.getInt("hp")
        binding.taskBottomBar.userExperienceBar.progress = arguments!!.getInt("exp")
    }

    private fun setListeners() {

        var clickCounter = 0
        var userAnswer = ""
        var rightAnswer = ""
        binding.getAnswerButton.answerBtn.setOnClickListener {
            // Счётчик нажатий
            clickCounter += 1
            // Пока не дошли до последнего элемента
            if (binding.taskViewPager.currentItem < binding.taskViewPager.adapter!!.itemCount - 1) {

                // Пока что эти аргументы не работают как надо, ниже есть костыльное решение
                /*val userAnswer = (binding.taskViewPager.adapter as TasksAdapter)
                    .getTaskFragment(binding.taskViewPager.currentItem)
                    ?.arguments?.getString("userAnswer") ?: "null"

                val rightAnswer = (binding.taskViewPager.adapter as TasksAdapter)
                    .getTaskFragment(binding.taskViewPager.currentItem)
                    ?.arguments?.getString("rightAnswer") ?: "null"*/

                val currentFragment = (binding.taskViewPager.adapter as TasksAdapter)
                    .getTaskFragment(binding.taskViewPager.currentItem)

                if (currentFragment is FourImageQuestion) {
                    userAnswer = currentFragment.userAnswer
                    rightAnswer = currentFragment.rightAnswer
                }

                if (currentFragment is SentenceBuildQuestion) {
                    userAnswer = currentFragment.userAnswer.joinToString { it.answer }
                    rightAnswer = currentFragment.rightAnswer.joinToString { it.answer }
                }

                // Если ответ выбран и получен
                if (userAnswer != "") {
                    showRightAnswer(userAnswer, rightAnswer)
                    binding.taskViewPager.currentItem += 1
                    if (userAnswer.compareTo(rightAnswer) != 0) {
                        // Если игрок ошибся, то у него отнимается 10 очков здоровья
                        binding.taskBottomBar.userHealthBar.progress -= 10
                        // Если у игрока закончилось здоровье
                        if (binding.taskBottomBar.userHealthBar.progress <= 0) {
                            dialog("Провал")
                            exitIntoLvl()
                        }
                    }

                    // Изменение прогресса
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
                    dialog("Пройдено")
                    // Пока что возвращается на главный экран
                    // Если пройдено успешно, то +20 очков опыта
                    binding.taskBottomBar.userExperienceBar.progress += 20
                    exitIntoLvl()
                }
            }

        binding.taskTopBar.closeButton.setOnClickListener {
            exitIntoLvl()
        }

    }

    /**
     * Метод, который выходит на главный экран
     * Так же передавая главному экрану информацию о количестве здоровья и опыта
     */
    private fun exitIntoLvl() {
        val hpAndExpProgress = Bundle()
        hpAndExpProgress.apply {
            putInt("hp", binding.taskBottomBar.userHealthBar.progress)
            putInt("exp", binding.taskBottomBar.userExperienceBar.progress)
        }
        findNavController().navigate(R.id.action_taskContainer_to_homePage, hpAndExpProgress)
    }

    private fun setTasks(taskList : List<Task>) {
        Log.i(MY_LOG_TAG, taskList.toString())
        val fragmentAdapter = TasksAdapter(requireActivity(), taskList)
        binding.taskViewPager.adapter = fragmentAdapter
        binding.taskViewPager.isUserInputEnabled = false
    }

    private fun showRightAnswer(userAnswer : String, rightAnswer : String) {

        val explanation =
            if (userAnswer.compareTo(rightAnswer) == 0) "Молодец!"
            else "Неверный ответ \n\n Правильный ответ: $rightAnswer " +
                    "\n Ваш ответ: $userAnswer \nВы теряете 10 ед. здоровья!"

        Log.i(MY_LOG_TAG, "Контейнер заданий: userAnswer = $userAnswer, rightAnswer = $rightAnswer")
        dialog(explanation)
    }

    private fun dialog(message: String) {
        val alertDialog = AlertDialog.Builder(requireActivity())
        alertDialog
            .setMessage(message)
            .create()
            .show()
    }

    private fun snackBar(message : String) {
        Snackbar.make(
            binding.gameTaskContainer,
            message,
            Snackbar.LENGTH_SHORT
        ).setTextColor(Color.GREEN).show()
    }

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