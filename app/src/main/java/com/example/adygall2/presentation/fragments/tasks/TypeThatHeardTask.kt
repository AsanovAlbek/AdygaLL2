package com.example.adygall2.presentation.fragments.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.example.adygall2.R
import com.example.adygall2.data.db_models.Answer
import com.example.adygall2.data.db_models.Sound
import com.example.adygall2.data.models.SoundsPlayer
import com.example.adygall2.databinding.FragmentTypeThatHeardBinding
import com.example.adygall2.presentation.GameViewModel
import com.example.adygall2.presentation.consts.ArgsKey.ID_KEY
import com.example.adygall2.presentation.consts.ArgsKey.SOUND_KEY
import com.example.adygall2.presentation.consts.ArgsKey.TASK_KEY
import org.koin.androidx.viewmodel.ext.android.viewModel

class TypeThatHeardTask : Fragment(R.layout.fragment_type_that_heard) {

    private lateinit var _binding : FragmentTypeThatHeardBinding
    private val binding get() = _binding
    private val viewModel by viewModel<GameViewModel>()
    private var _rightAnswer = ""
    val rightAnswer get() = _rightAnswer
    private var _userAnswer = ""
    val userAnswer get() = _userAnswer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTypeThatHeardBinding.inflate(inflater, container, false)

        binding.taskText.text = arguments?.getString(TASK_KEY)
        setObservers()
        // Слушатель на изменения в тексте
        binding.textInputField.doAfterTextChanged {
            _userAnswer = it.toString()
        }

        return binding.root
    }

    private fun setObservers() {
        val taskId = arguments?.getInt(ID_KEY)
        val soundId = arguments?.getInt(SOUND_KEY)
        viewModel.getAnswers(taskId!!)
        viewModel.answersListFromDb.observe(viewLifecycleOwner) {
            _rightAnswer = it.joinToString { answer -> answer.answer }
        }

        viewModel.getSoundById(soundId!!)
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
            if (binding.soundButtons.playSoundButton.icon.current == playSoundDrawable) {
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
    }
}