package com.example.adygall2.presentation.fragments

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.adygall2.R
import com.example.adygall2.data.db_models.Answer
import com.example.adygall2.data.db_models.Sound
import com.example.adygall2.databinding.FragmentWordsQuestionBinding
import com.example.adygall2.presentation.GameViewModel
import com.example.adygall2.presentation.adapters.SecondSentenceAdapter
import com.example.adygall2.presentation.adapters.SentenceAdapter
import com.example.adygall2.presentation.consts.ArgsKey
import com.example.adygall2.presentation.consts.ArgsKey.MY_LOG_TAG
import com.example.adygall2.presentation.consts.ArgsKey.TASK_KEY
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.FlexboxLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.FileNotFoundException

class SentenceBuildQuestion : Fragment(R.layout.fragment_words_question) {

    private lateinit var _binding: FragmentWordsQuestionBinding
    private val binding get() = _binding
    private val viewModel by viewModel<GameViewModel>()

    private var _userAnswer = mutableListOf<Answer>()
    val userAnswer get() = _userAnswer
    private var _rightAnswer = listOf<Answer>()
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

        val taskId = arguments?.getInt(ArgsKey.ID_KEY)!!

        viewModel.getAnswers(taskId)
        viewModel.answersListFromDb.observe(viewLifecycleOwner, ::setAdapter)

        viewModel.getSoundById(taskId)
        viewModel.soundFromDb.observe(viewLifecycleOwner, ::playSound)
    }

    private fun playSound(sound : Sound) {
        binding.happyManIcon.setOnClickListener {
            val mediaPlayer = MediaPlayer()
            val audioManager = requireActivity().getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
            val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            val volume = (currentVolume / maxVolume).toFloat()

            try {
                val afd = requireActivity().assets.openFd("sounds/${sound.filePath}")
                mediaPlayer.apply {
                    setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                    // Передаём 2 раза одну переменную, потому что это и левая громкость, и правая
                    setVolume(volume,volume)
                    prepare()
                    seekTo(0)
                    start()
                }
                afd.close()
            }
            catch (ex : FileNotFoundException) {
                Log.i("zhopa", "Файл не найден, ${ex.stackTrace}")
            }
        }
    }

    private fun setAdapter(answers : List<Answer>) {

        Log.i(MY_LOG_TAG, "answerCount = ${answers.size}, answers = $answers")

        val userAdapter = SentenceAdapter(answers)
        val answerAdapter = SecondSentenceAdapter(mutableListOf(), userAdapter)
        (binding.answerRecView.layoutManager as FlexboxLayoutManager)
            .flexDirection = FlexboxLayout.LAYOUT_DIRECTION_LTR

        binding.wordsRecView.adapter = userAdapter
        binding.answerRecView.adapter = answerAdapter

        _userAnswer = answerAdapter.answerList
        _rightAnswer = answers.filter { answer -> answer.isCorrectAnswer }

        val bundle = Bundle()
        bundle.apply {
            putString("rightAnswer", rightAnswer.map { it.answer }.toString())
            putString("userAnswer", userAnswer.map { it.answer }.toString())
        }
        this.arguments = bundle
    }

    private fun saveBundle() {

    }
}