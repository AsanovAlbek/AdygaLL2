package com.example.adygall2.presentation.fragments.tasks

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.adygall2.R
import com.example.adygall2.data.db_models.Answer
import com.example.adygall2.data.db_models.Sound
import com.example.adygall2.data.models.SoundsPlayer
import com.example.adygall2.databinding.FragmentWordsQuestionBinding
import com.example.adygall2.presentation.adapters.SentenceAdapter
import com.example.adygall2.presentation.adapters.adapter_handles.AdapterCallback
import com.example.adygall2.presentation.adapters.adapter_handles.HandleDragAndDropEvent
import com.example.adygall2.presentation.view_model.GameViewModel
import com.example.adygall2.presentation.consts.ArgsKey.ID_KEY
import com.example.adygall2.presentation.consts.ArgsKey.SOUND_KEY
import com.example.adygall2.presentation.consts.ArgsKey.TASK_KEY
import com.example.adygall2.presentation.fragments.tasks.base_task.BaseTaskFragment
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel

/** Фрагмент для задания с построением предложения по услышанному */
class SentenceBuildQuestion(
    private val skipListener: (() -> Unit)
) : BaseTaskFragment(R.layout.fragment_words_question), AdapterCallback {

    private lateinit var _sentenceBuildBinding: FragmentWordsQuestionBinding
    private val sentenceBuildBinding get() = _sentenceBuildBinding
    private val viewModel by viewModel<GameViewModel>()

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

        sentenceBuildBinding.wordsTaskText.text = arguments?.getString(TASK_KEY)

        setObservers()

        return sentenceBuildBinding.root
    }

    private fun setObservers() {

        val taskId = arguments?.getInt(ID_KEY)!!
        val soundId = arguments?.getInt(SOUND_KEY)!!

        viewModel.getAnswers(taskId)
        viewModel.answersListFromDb.observe(viewLifecycleOwner, ::setAdapter)

        viewModel.getSoundById(soundId)
        viewModel.soundFromDb.observe(viewLifecycleOwner, ::setSoundButtonsListeners)
    }

    private fun setSoundButtonsListeners(sound: Sound) {
        // Экземпляр класса для работы с воспроизведением звука
        val soundsPlayer = SoundsPlayer(requireActivity())

        // При нажатии меняем иконку на кнопке
        val playSoundDrawable =
            ResourcesCompat.getDrawable(resources, R.drawable.play_sound, null)
        val stopSoundDrawable =
            ResourcesCompat.getDrawable(resources, R.drawable.stop_play, null)
        // Как воспроизведение заканчивается, иконка меняется
        soundsPlayer.mediaPlayer.setOnCompletionListener {
            sentenceBuildBinding.soundButtons.playSoundButton.icon = playSoundDrawable
            soundsPlayer.mediaPlayer.reset()
        }

        // Слушатель нажатий на кнопку проигрывания озвучки
        // При нажатиях меняется картинка
        sentenceBuildBinding.soundButtons.playSoundButton.setOnClickListener {
            if (sentenceBuildBinding.soundButtons.playSoundButton.icon.equals(stopSoundDrawable)) {
                sentenceBuildBinding.soundButtons.playSoundButton.icon = playSoundDrawable
                soundsPlayer.stopPlay()
            }
            else {
                soundsPlayer.playbackSpeed = SoundsPlayer.NORMAL_PLAYBACK
                sentenceBuildBinding.soundButtons.playSoundButton.icon = stopSoundDrawable
                soundsPlayer.playSound(sound)
            }
        }

        // Слушатель нажатия медленного проигрывания озвучки
        sentenceBuildBinding.soundButtons.slowPlayButton.setOnClickListener {
            if (soundsPlayer.mediaPlayer.isPlaying) {
                soundsPlayer.stopPlay()
            }
            else {
                soundsPlayer.playbackSpeed = SoundsPlayer.SLOW_PLAYBACK
                soundsPlayer.playSound(sound)
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
}