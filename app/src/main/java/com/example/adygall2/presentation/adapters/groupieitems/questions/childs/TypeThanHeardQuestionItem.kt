package com.example.adygall2.presentation.adapters.groupieitems.questions.childs

import android.content.Context
import android.view.View
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import com.example.adygall2.R
import com.example.adygall2.data.models.SoundsPlayer
import com.example.adygall2.databinding.FragmentTypeThatHeardBinding
import com.example.adygall2.domain.model.ComplexAnswer
import com.example.adygall2.domain.model.Source
import com.example.adygall2.presentation.adapters.groupieitems.questions.parentitem.QuestionItem

class TypeThanHeardQuestionItem(
    private val context: Context,
    private val title: String,
    private val answers: List<ComplexAnswer>,
    private val soundsPlayer: SoundsPlayer,
    private val playerSource: Source
): QuestionItem<FragmentTypeThatHeardBinding>() {
    private var _userAnswer = ""
    override val userAnswer: String get() = _userAnswer
    private lateinit var textField: EditText

    override val rightAnswer: String = answers.joinToString { it.answer.answer }

    override val onNextQuestion: () -> Unit
        get() = {
            _userAnswer = ""
            textField.setText("")
        }

    override fun getLayout(): Int = R.layout.fragment_type_that_heard

    override fun initializeViewBinding(view: View): FragmentTypeThatHeardBinding =
        FragmentTypeThatHeardBinding.bind(view)

    override fun bind(viewBinding: FragmentTypeThatHeardBinding, position: Int) {
        setupSoundButtons(viewBinding)
        textChangedListener(viewBinding)
    }

    private fun textChangedListener(viewBinding: FragmentTypeThatHeardBinding) {
        textField = viewBinding.textInputField
        viewBinding.textInputField.doAfterTextChanged {
            _userAnswer = it.toString()
        }
    }

    private fun setupSoundButtons(viewBinding: FragmentTypeThatHeardBinding) {
        val playSoundDrawable = context.getDrawable(R.drawable.play_sound)
        val stopSoundDrawable = context.getDrawable(R.drawable.stop_play)

        soundsPlayer.apply {
            viewBinding.soundButtons.apply {
                setCompletionListener {
                    if (playSoundButton.icon.equals(stopSoundDrawable)) {
                        playSoundButton.icon = playSoundDrawable
                    }
                    reset()
                }

                playSoundButton.setOnClickListener {
                    if (isPlayingNow) {
                        stopPlay()
                        if(playSoundButton.icon.equals(stopSoundDrawable)) {
                            playSoundButton.icon = playSoundDrawable
                        }
                    } else {
                        normalPlaybackSpeed()
                        playSoundButton.icon = stopSoundDrawable
                        playSound(playerSource)
                    }
                }

                slowPlayButton.setOnClickListener {
                    if (isPlayingNow) {
                        stopPlay()
                    } else {
                        slowPlaybackSpeed()
                        playSound(playerSource)
                    }
                }
            }
        }
    }
}