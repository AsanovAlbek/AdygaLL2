package com.example.adygall2.presentation.fragments.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doAfterTextChanged
import com.example.adygall2.R
import com.example.adygall2.data.models.SoundsPlayer
import com.example.adygall2.databinding.FragmentTypeThatHeardBinding
import com.example.adygall2.domain.model.Answer
import com.example.adygall2.domain.model.Source
import com.example.adygall2.presentation.view_model.GameViewModel
import com.example.adygall2.presentation.consts.ArgsKey.ID_KEY
import com.example.adygall2.presentation.consts.ArgsKey.SOUND_KEY
import com.example.adygall2.presentation.consts.ArgsKey.TASK_KEY
import com.example.adygall2.presentation.fragments.tasks.base_task.BaseTaskFragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/** Фрагмент для задания с написанием услышанного */
class TypeThatHeardFragment : BaseTaskFragment(R.layout.fragment_type_that_heard) {

    private var _typeThatHeardBinding : FragmentTypeThatHeardBinding? = null
    private val typeThatHeardBinding get() = _typeThatHeardBinding!!
    private val viewModel by viewModel<GameViewModel>()
    private var _rightAnswer = ""
    override val rightAnswer get() = _rightAnswer
    private var _userAnswer = ""
    override val userAnswer get() = _userAnswer
    private val soundsPlayer by inject<SoundsPlayer>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _typeThatHeardBinding = FragmentTypeThatHeardBinding.inflate(inflater, container, false)
        return typeThatHeardBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        typeThatHeardBinding.taskText.text = arguments?.getString(TASK_KEY)
        setObservers()
        getDataFromViewModel()
        // Слушатель на изменения в тексте
        textChangeListener()
    }

    private fun setObservers() {
        viewModel.answersListFromDb.observe(viewLifecycleOwner, ::setupAnswers)
        viewModel.soundFromDb.observe(viewLifecycleOwner, ::setSoundButtonsListeners)
    }

    private fun setupAnswers(answers: MutableList<Answer>) {
        val rightAnswerStroke = answers.joinToString { answer -> answer.answer }
        _rightAnswer = viewModel.transform(rightAnswerStroke)
    }

    private fun textChangeListener() {
        typeThatHeardBinding.textInputField.doAfterTextChanged {
            val userAnswerStroke = it.toString()
            _userAnswer = viewModel.transform(userAnswerStroke)
        }
    }

    private fun getDataFromViewModel() {
        val taskId = arguments?.getInt(ID_KEY)
        val soundId = arguments?.getInt(SOUND_KEY)
        viewModel.getAnswers(taskId!!)
        viewModel.getSoundById(soundId!!)
    }

    private fun setSoundButtonsListeners(sound: Source) {
        // При нажатии меняем иконку на кнопке
        val playSoundDrawable =
            ResourcesCompat.getDrawable(resources, R.drawable.play_sound, null)
        val stopSoundDrawable =
            ResourcesCompat.getDrawable(resources, R.drawable.stop_play, null)
        // Как воспроизведение заканчивается, иконка меняется
        soundsPlayer.setCompletionListener {
            typeThatHeardBinding.soundButtons.playSoundButton.icon = playSoundDrawable
            soundsPlayer.reset()
        }

        typeThatHeardBinding.soundButtons.playSoundButton.setOnClickListener {
            if (soundsPlayer.isPlayingNow) {
                soundsPlayer.stopPlay()
                if (typeThatHeardBinding.soundButtons.playSoundButton.icon.equals(stopSoundDrawable)) {
                    typeThatHeardBinding.soundButtons.playSoundButton.icon = playSoundDrawable
                }
            }

            else {
                soundsPlayer.normalPlaybackSpeed()
                typeThatHeardBinding.soundButtons.playSoundButton.icon = stopSoundDrawable
                soundsPlayer.playSound(sound)
            }
        }

        typeThatHeardBinding.soundButtons.slowPlayButton.setOnClickListener {
            if (soundsPlayer.isPlayingNow) {
                soundsPlayer.stopPlay()
            }
            else {
                soundsPlayer.slowPlaybackSpeed()
                soundsPlayer.playSound(sound)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        _userAnswer = ""
        _rightAnswer = ""
        typeThatHeardBinding.textInputField.setText("")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _typeThatHeardBinding = null
    }
}