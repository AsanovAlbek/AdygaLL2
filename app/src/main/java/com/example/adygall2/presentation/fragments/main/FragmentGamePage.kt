package com.example.adygall2.presentation.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import com.example.adygall2.R
import com.example.adygall2.databinding.TaskContainerBinding
import com.example.adygall2.presentation.view_model.GameViewModel
import com.example.adygall2.presentation.adapters.groupieitems.questions.parentitem.QuestionItem
import com.example.adygall2.presentation.model.DialogState
import com.example.adygall2.presentation.model.GameState
import com.xwray.groupie.GroupieAdapter
import kotlinx.coroutines.launch

import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Фрагмент для игрового процесса
 */
class FragmentGamePage : Fragment(R.layout.task_container) {

    private lateinit var _taskContainerBinding: TaskContainerBinding
    private val taskContainerBinding get() = _taskContainerBinding
    private val viewModel by viewModel<GameViewModel>()
    private val gameArgs: FragmentGamePageArgs by navArgs()
    private lateinit var tasksAdapter: GroupieAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _taskContainerBinding = TaskContainerBinding.inflate(inflater, container, false)
        return taskContainerBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observe()
        taskContainerBinding.taskBottomBar.userAvatar.setImageBitmap(viewModel.getPhotoFromCache())
        // Присвоение нажатия кнопке выхода
        taskContainerBinding.taskTopBar.closeButton.setOnClickListener {
            // Создание диалога для выхода
            viewModel.exit(
                context = requireContext(),
                navController = findNavController()
            )
        }

        tasksAdapter = GroupieAdapter()
        viewModel.start(
            context = requireContext().applicationContext,
            tasks = gameArgs.tasks.toList(),
            lesson = gameArgs.lessonProgress,
            level = gameArgs.levelProgress
        )

        taskContainerBinding.taskViewPager.apply {
            adapter = tasksAdapter
            isUserInputEnabled = false
        }

        taskContainerBinding.taskBottomBar.apply {
            userNameTv.text = viewModel.user.name
            userAvatar.setImageBitmap(viewModel.getPhotoFromCache())
            viewModel.initAutoHill(hp.progress)
        }

        hillHp()
    }

    /**
     * При скрытии фрагмента чистится кэш картинок
     */
    override fun onPause() {
        super.onPause()
        viewModel.clearGlideCache()
        taskContainerBinding.taskBottomBar.apply {
            viewModel.saveUserStates(hp = hp.progress, coins = exp.progress)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tasksAdapter.clear()
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
        viewModel.items.observe(viewLifecycleOwner, ::observeItems)
        viewModel.gameState.observe(viewLifecycleOwner, ::observeGameState)
        viewModel.dialogState.observe(viewLifecycleOwner, ::observeDialogState)
    }

    private fun observeGameState(gameState: GameState) {
        taskContainerBinding.apply {
            taskViewPager.currentItem = gameState.currentQuestionPosition
            soundTaskSkip.isVisible = gameState.canSkipTask
            taskTopBar.LevelNumber.text = gameState.lessonTitle
            taskBottomBar.hp.progress = gameState.hp
            taskBottomBar.exp.progress = gameState.coins
            taskTopBar.progressIndicator.progress = gameState.currentQuestionPosition + 1
        }
    }

    /** Слежение за изменением состояния диалогового окна */
    private fun observeDialogState(dialogState: DialogState) {
        taskContainerBinding.apply {
            dialogShowAnswer.apply {
                root.isVisible = dialogState.rootVisible
                accuracy.setText(dialogState.accuracy)
                accuracy.setTextColor(ContextCompat.getColor(requireContext(), dialogState.accuracyTextColor))
                correctAnswer.text = dialogState.correctAnswer
                icon.setImageResource(dialogState.iconId)
                rightTextIs.isVisible = dialogState.rightTextVisible
                correctAnswer.isVisible = dialogState.correctAnswerVisible
                nextButton.setOnClickListener { dialogState.buttonAction() }
            }
            taskViewPager.isEnabled = dialogState.viewPagerEnabled
            getAnswerButton.root.isEnabled = dialogState.answerButtonEnabled
        }
    }

    private fun observeItems(questionItems: MutableList<QuestionItem<out ViewBinding>?>) {
        tasksAdapter.update(questionItems)
        setListeners()
    }

    /**
     * Метод для присвоения обработчиков нажатий
     */
    private fun setListeners() {
        taskContainerBinding.apply {
            getAnswerButton.answerBtn.setOnClickListener {
                viewModel.getAnswerButtonClickAction(
                    requireView = requireView(),
                    navController = findNavController(),
                    context = requireContext(),
                    tasks = gameArgs.tasks.toList(),
                    level = gameArgs.levelProgress,
                    lesson = gameArgs.lessonProgress,
                    coinsBeforeLesson = viewModel.user.coins,
                    coins = taskContainerBinding.taskBottomBar.exp.progress,
                    hp = taskContainerBinding.taskBottomBar.hp.progress
                )
            }
            soundTaskSkip.setOnClickListener {
                viewModel.skipQuestion(
                    tasks = gameArgs.tasks.toList(),
                    level = gameArgs.levelProgress,
                    lesson = gameArgs.lessonProgress,
                    coinsBeforeLesson = viewModel.user.coins,
                    coins = taskContainerBinding.taskBottomBar.exp.progress,
                    hp = taskContainerBinding.taskBottomBar.hp.progress,
                    navController = findNavController()
                )
            }
        }
    }
}