package com.example.adygall2.presentation.fragments.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.example.adygall2.R
import com.example.adygall2.data.db_models.Sound
import com.example.adygall2.data.models.SoundsPlayer
import com.example.adygall2.databinding.FragmentTypeThatHeardBinding
import com.example.adygall2.presentation.view_model.GameViewModel
import com.example.adygall2.presentation.consts.ArgsKey.ID_KEY
import com.example.adygall2.presentation.consts.ArgsKey.SOUND_KEY
import com.example.adygall2.presentation.consts.ArgsKey.TASK_KEY
import org.koin.androidx.viewmodel.ext.android.viewModel

class TypeThatHeardTask : Fragment(R.layout.fragment_type_that_heard) {

    private lateinit var _typeThatHeardBinding : FragmentTypeThatHeardBinding
    private val typeThatHeardBinding get() = _typeThatHeardBinding
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
        _typeThatHeardBinding = FragmentTypeThatHeardBinding.inflate(inflater, container, false)

        typeThatHeardBinding.taskText.text = arguments?.getString(TASK_KEY)
        setObservers()
        // Слушатель на изменения в тексте
        typeThatHeardBinding.textInputField.doAfterTextChanged {
            val userAnswerStroke = it.toString()
            _userAnswer = viewModel.transform(userAnswerStroke)
        }

        return typeThatHeardBinding.root
    }

    private fun setObservers() {
        val taskId = arguments?.getInt(ID_KEY)
        val soundId = arguments?.getInt(SOUND_KEY)
        viewModel.getAnswers(taskId!!)
        viewModel.answersListFromDb.observe(viewLifecycleOwner) {
            val rightAnswerStroke = it.joinToString { answer -> answer.answer }
            _rightAnswer = viewModel.transform(rightAnswerStroke)
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
            typeThatHeardBinding.soundButtons.playSoundButton.icon = playSoundDrawable
            soundsPlayer.mediaPlayer.reset()
        }

        typeThatHeardBinding.soundButtons.playSoundButton.setOnClickListener {
            if (typeThatHeardBinding.soundButtons.playSoundButton.icon.equals(stopSoundDrawable)) {
                typeThatHeardBinding.soundButtons.playSoundButton.icon = playSoundDrawable
                soundsPlayer.stopPlay()
            }
            else {
                soundsPlayer.playbackSpeed = SoundsPlayer.NORMAL_PLAYBACK
                typeThatHeardBinding.soundButtons.playSoundButton.icon = stopSoundDrawable
                soundsPlayer.playSound(sound)
            }
        }

        typeThatHeardBinding.soundButtons.slowPlayButton.setOnClickListener {
            if (soundsPlayer.mediaPlayer.isPlaying) {
                soundsPlayer.stopPlay()
            }
            else {
                soundsPlayer.playbackSpeed = SoundsPlayer.SLOW_PLAYBACK
                soundsPlayer.playSound(sound)
            }
        }
    }
}