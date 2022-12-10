package com.example.adygall2.presentation.fragments.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.example.adygall2.R
import com.example.adygall2.domain.model.Answer
import com.example.adygall2.data.models.SoundsPlayer
import com.example.adygall2.databinding.FragmentWordsQuestionBinding
import com.example.adygall2.domain.model.Source
import com.example.adygall2.presentation.adapters.SentenceAdapter
import com.example.adygall2.presentation.adapters.adapter_handles.AdapterHandleDragAndDropCallback
import com.example.adygall2.presentation.adapters.adapter_handles.HandleDragAndDropEvent
import com.example.adygall2.presentation.view_model.GameViewModel
import com.example.adygall2.presentation.consts.ArgsKey.ID_KEY
import com.example.adygall2.presentation.consts.ArgsKey.SOUND_KEY
import com.example.adygall2.presentation.consts.ArgsKey.TASK_KEY
import com.example.adygall2.presentation.fragments.tasks.base_task.BaseTaskFragment
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/** Фрагмент для задания с построением предложения по услышанному */
class SentenceBuildFragment(
    private val skipListener: (() -> Unit)
) : BaseTaskFragment(R.layout.fragment_words_question), AdapterHandleDragAndDropCallback {

    private var _sentenceBuildBinding: FragmentWordsQuestionBinding? = null
    private val sentenceBuildBinding get() = _sentenceBuildBinding!!
    private val viewModel by viewModel<GameViewModel>()
    private val soundsPlayer : SoundsPlayer by inject()

    private var _userAnswer = String()
    override val userAnswer get() = _userAnswer
    private var _rightAnswer = String()
    override val rightAnswer get() = _rightAnswer

    private lateinit var userAdapter : SentenceAdapter
    private lateinit var answerAdapter : SentenceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _sentenceBuildBinding = FragmentWordsQuestionBinding.inflate(inflater, container, false)
        return sentenceBuildBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sentenceBuildBinding.wordsTaskText.text = arguments?.getString(TASK_KEY)
        setObservers()
        dataFromViewModel()
    }

    private fun setObservers() {
        viewModel.answersListFromDb.observe(viewLifecycleOwner, ::setAdapter)
        viewModel.soundFromDb.observe(viewLifecycleOwner, ::setSoundButtonsListeners)
    }

    private fun dataFromViewModel() {
        val soundId = arguments?.getInt(SOUND_KEY)!!
        viewModel.getSoundById(soundId)

        val taskId = arguments?.getInt(ID_KEY)!!
        viewModel.getAnswers(taskId)
    }

    private fun setSoundButtonsListeners(source: Source) {
        // При нажатии меняем иконку на кнопке
        val playSoundDrawable =
            ResourcesCompat.getDrawable(resources, R.drawable.play_sound, null)
        val stopSoundDrawable =
            ResourcesCompat.getDrawable(resources, R.drawable.stop_play, null)
        // Как воспроизведение заканчивается, иконка меняется
        soundsPlayer.setCompletionListener {
            sentenceBuildBinding.soundButtons.playSoundButton.icon = playSoundDrawable
            soundsPlayer.reset()
        }

        // Слушатель нажатий на кнопку проигрывания озвучки
        // При нажатиях меняется картинка
        sentenceBuildBinding.soundButtons.playSoundButton.setOnClickListener {
            if (soundsPlayer.isPlayingNow) {
                soundsPlayer.stopPlay()
                if (sentenceBuildBinding.soundButtons.playSoundButton.icon.equals(stopSoundDrawable)) {
                    sentenceBuildBinding.soundButtons.playSoundButton.icon = playSoundDrawable
                }
            }

            else {
                soundsPlayer.normalPlaybackSpeed()
                sentenceBuildBinding.soundButtons.playSoundButton.icon = stopSoundDrawable
                soundsPlayer.playSound(source)
            }
        }

        // Слушатель нажатия медленного проигрывания озвучки
        sentenceBuildBinding.soundButtons.slowPlayButton.setOnClickListener {
            if (soundsPlayer.isPlayingNow) {
                soundsPlayer.stopPlay()
                sentenceBuildBinding.soundButtons.playSoundButton.icon = playSoundDrawable
            }
            else {
                soundsPlayer.slowPlaybackSpeed()
                soundsPlayer.playSound(source)
            }
        }

        sentenceBuildBinding.soundTaskSkip.setOnClickListener { skipListener.invoke() }
    }

    private fun setAdapter(answers: MutableList<Answer>) {

        val mutableAnswers = answers.map { it.answer }.toMutableList()
        userAdapter = SentenceAdapter(requireContext(), true, mutableAnswers, this)
        answerAdapter = SentenceAdapter(requireContext(), false, mutableListOf(), this)

        val answerLayoutManager = FlexboxLayoutManager(requireContext())
        answerLayoutManager.apply {
            flexDirection = FlexDirection.ROW
        }

        sentenceBuildBinding.answerRecView.layoutManager = answerLayoutManager

        sentenceBuildBinding.answerRecView.adapter = answerAdapter
        sentenceBuildBinding.wordsRecView.adapter = userAdapter

        sentenceBuildBinding.wordsRecView.setOnDragListener { _, dragEvent ->
            HandleDragAndDropEvent(dragEvent).handle(this, true, -1)
            true
        }

        sentenceBuildBinding.answerRecView.setOnDragListener { _, dragEvent ->
            HandleDragAndDropEvent(dragEvent).handle(this, false, -1)
            true
        }

        val rightAnswerStroke = answers.first().correctAnswer
        _rightAnswer = viewModel.transform(rightAnswerStroke)
    }

    /** Метод для изменения содержимого адаптеров */
    override fun change(isFirstAdapter: Boolean, item: String, position: Int) {
        if (isFirstAdapter) {
            userAdapter.addAnswer(item, position)
            answerAdapter.removeAnswer(item)
        }
        else {
            answerAdapter.addAnswer(item, position)
            userAdapter.removeAnswer(item)
        }
        _userAnswer = viewModel.transform(answerAdapter.adapterItems.joinToString())
    }

    override fun onPause() {
        super.onPause()
        _userAnswer = ""
        _rightAnswer = ""
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _sentenceBuildBinding = null
    }
}