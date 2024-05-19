package com.example.adygall2.presentation.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.adygall2.R
import com.example.adygall2.data.worker.UserHillWorker
import com.example.adygall2.databinding.TaskContainerBinding
import com.example.adygall2.presentation.activities.MainActivity
import com.example.adygall2.presentation.activities.UserChangeListener
import com.example.adygall2.presentation.view_model.GameViewModel
import com.example.adygall2.presentation.adapters.groupieitems.questions.parentitem.QuestionItem
import com.example.adygall2.presentation.model.DialogState
import com.example.adygall2.presentation.model.GameState
import com.xwray.groupie.GroupieAdapter
import java.util.concurrent.TimeUnit

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
    private var userChangeListener: UserChangeListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _taskContainerBinding = TaskContainerBinding.inflate(inflater, container, false)
        userChangeListener = requireActivity() as MainActivity
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
            lesson = gameArgs.lessonProgress,
            level = gameArgs.levelProgress,
            levelName = gameArgs.levelName,
        )

        taskContainerBinding.taskViewPager.apply {
            adapter = tasksAdapter
            isUserInputEnabled = false
        }

        taskContainerBinding.taskBottomBar.apply {
            userAvatar.setImageBitmap(viewModel.getPhotoFromCache())
        }
    }

    /**
     * При скрытии фрагмента чистится кэш картинок
     */
    override fun onPause() {
        viewModel.clearGlideCache()
        super.onPause()
    }

    override fun onStop() {
        taskContainerBinding.taskBottomBar.apply {
            viewModel.saveUserStates(hp = hp.progress, coins = exp.progress)
        }
        super.onStop()
    }

    override fun onDestroyView() {
        tasksAdapter.clear()
        userChangeListener?.getUserHealthLiveData()?.removeObservers(viewLifecycleOwner)
        userChangeListener = null
        super.onDestroyView()
    }

    private fun observe() {
        viewModel.items.observe(viewLifecycleOwner, ::observeItems)
        viewModel.gameState.observe(viewLifecycleOwner, ::observeGameState)
        viewModel.dialogState.observe(viewLifecycleOwner, ::observeDialogState)
        userChangeListener?.getUserHealthLiveData()?.observe(viewLifecycleOwner, ::observeHealth)
    }

    private fun initWorker() {
        val workRequest = PeriodicWorkRequestBuilder<UserHillWorker>(10, TimeUnit.MINUTES)
        WorkManager.getInstance(requireContext()).enqueue(workRequest.build())
    }

    private fun observeHealth(healthValue: Int) {
        taskContainerBinding.taskBottomBar.hp.progress = healthValue
    }

    private fun observeGameState(gameState: GameState) {
        taskContainerBinding.apply {
            //taskBottomBar.hp.progress = gameState.hp
            loading.isVisible = gameState.isLoading
            gameContent.isVisible = !gameState.isLoading
            taskBottomBar.userNameTv.text = gameState.userName
            taskViewPager.currentItem = gameState.currentQuestionPosition
            soundTaskSkip.isVisible = gameState.canSkipTask
            taskTopBar.LevelNumber.text = gameState.lessonTitle
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
                accuracy.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        dialogState.accuracyTextColor
                    )
                )
                correctAnswer.text = dialogState.correctAnswer
                icon.setImageResource(dialogState.iconId)
                rightTextIs.isVisible = dialogState.rightTextVisible
                correctAnswer.isVisible = dialogState.correctAnswerVisible
                nextButton.setOnClickListener { dialogState.buttonAction() }
            }
            taskViewPager.isEnabled = dialogState.viewPagerEnabled
            getAnswerButton.isEnabled = dialogState.answerButtonEnabled
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
            getAnswerButton.setOnClickListener {
                viewModel.getAnswerButtonClickAction(
                    requireView = requireView(),
                    navController = findNavController(),
                    context = requireContext(),
                    level = gameArgs.levelProgress,
                    lesson = gameArgs.lessonProgress,
                    coinsBeforeLesson = viewModel.user.coins,
                    coins = taskContainerBinding.taskBottomBar.exp.progress,
                    hp = taskContainerBinding.taskBottomBar.hp.progress,
                    userHealthHandle = userChangeListener!!
                )
            }
            soundTaskSkip.setOnClickListener {
                viewModel.skipQuestion(
                    level = gameArgs.levelProgress,
                    lesson = gameArgs.lessonProgress,
                    coinsBeforeLesson = viewModel.user.coins,
                    coins = taskContainerBinding.taskBottomBar.exp.progress,
                    hp = taskContainerBinding.taskBottomBar.hp.progress,
                    navController = findNavController(),
                )
            }
        }
    }
}