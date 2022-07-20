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
import com.example.adygall2.presentation.GameViewModel
import com.example.adygall2.presentation.adapters.SecondSentenceAdapter
import com.example.adygall2.presentation.adapters.SentenceAdapter
import com.example.adygall2.presentation.consts.ArgsKey.ID_KEY
import com.example.adygall2.presentation.consts.ArgsKey.MY_LOG_TAG
import com.example.adygall2.presentation.consts.ArgsKey.SOUND_KEY
import com.example.adygall2.presentation.consts.ArgsKey.TASK_KEY
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel

class SentenceBuildQuestion(
    private val skipListener : (() -> Unit)
) : Fragment(R.layout.fragment_words_question) {

    private lateinit var _binding: FragmentWordsQuestionBinding
    private val binding get() = _binding
    private val viewModel by viewModel<GameViewModel>()

    private var _userAnswer = listOf<Answer>()
    val userAnswer get() = _userAnswer
    private var _rightAnswer = listOf<String>()
    val rightAnswer get() = _rightAnswer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(MY_LOG_TAG, "Задание с построением предложения создано")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentWordsQuestionBinding.inflate(inflater, container, false)

        binding.wordsTaskText.text = arguments?.getString(TASK_KEY)

        setObservers()
        saveBundle()

        return binding.root
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
            binding.soundButtons.playSoundButton.icon = playSoundDrawable
            soundsPlayer.mediaPlayer.reset()
        }

        binding.soundButtons.playSoundButton.setOnClickListener {
            if (binding.soundButtons.playSoundButton.icon.equals(playSoundDrawable)) {
                binding.soundButtons.playSoundButton.icon = stopSoundDrawable
                soundsPlayer.playSound(sound)
            } else {
                binding.soundButtons.playSoundButton.icon = playSoundDrawable
                soundsPlayer.stopPlay()
            }
        }

        binding.soundButtons.slowPlayButton.setOnClickListener {
            soundsPlayer.playbackSpeed = SoundsPlayer.SLOW_PLAYBACK
        }

        binding.soundButtons.normalPlayButton.setOnClickListener {
            soundsPlayer.playbackSpeed = SoundsPlayer.NORMAL_PLAYBACK
        }

        binding.soundTaskSkip.setOnClickListener { skipListener.invoke() }
    }

    private fun setAdapter(answers: List<Answer>) {

        Log.i(MY_LOG_TAG, "answerCount = ${answers.size}, answers = $answers")

        val userAdapter = SentenceAdapter(answers)
        val answerAdapter = SecondSentenceAdapter(mutableListOf(), userAdapter)

        (binding.answerRecView.layoutManager as FlexboxLayoutManager)
            .flexDirection = FlexDirection.ROW

        binding.wordsRecView.adapter = userAdapter
        binding.answerRecView.adapter = answerAdapter

        _userAnswer = answerAdapter.answerList
        _rightAnswer = answers.first().correctAnswer.split(" ", ",")
    }

    private fun saveBundle() {

    }
}