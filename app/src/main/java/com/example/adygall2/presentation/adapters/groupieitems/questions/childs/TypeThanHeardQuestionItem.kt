package com.example.adygall2.presentation.adapters.groupieitems.questions.childs

import android.content.Context
import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.example.adygall2.R
import com.example.adygall2.data.delegate.AnswerFormatterImpl
import com.example.adygall2.data.models.SoundsPlayer
import com.example.adygall2.databinding.FragmentTypeThatHeardBinding
import com.example.adygall2.domain.model.ComplexAnswer
import com.example.adygall2.domain.model.Source
import com.example.adygall2.domain.model.Task
import com.example.adygall2.presentation.adapters.groupieitems.questions.parentitem.QuestionItem

class TypeThanHeardQuestionItem(
    private val context: Context,
    private val title: String,
    private val answers: List<ComplexAnswer>,
    private val playerSource: Source
) : QuestionItem<FragmentTypeThatHeardBinding>() {
    private var _userAnswer = ""
    override val userAnswer: String get() = _userAnswer.replace("[1iLlI|]".toRegex(), "I")
    private var textField: EditText? = null
    private var player: SoundsPlayer? = null

    override val rightAnswer: String = answers.joinToString { it.answer.answer }.replace("[1iLlI|]".toRegex(), "I")

    override fun getLayout(): Int = R.layout.fragment_type_that_heard

    override fun initializeViewBinding(view: View): FragmentTypeThatHeardBinding =
        FragmentTypeThatHeardBinding.bind(view)

    override fun bind(viewBinding: FragmentTypeThatHeardBinding, position: Int) {
        player = SoundsPlayer(context)
        setupSoundButtons(viewBinding)
        textChangedListener(viewBinding)
        viewBinding.taskText.text = title
    }

    private fun textChangedListener(viewBinding: FragmentTypeThatHeardBinding) {
        textField = viewBinding.textInputField
        viewBinding.textInputField.doAfterTextChanged {
            _userAnswer = it.toString()
        }
    }

    private fun setupSoundButtons(viewBinding: FragmentTypeThatHeardBinding) {
        val playSoundDrawable = ContextCompat.getDrawable(context,R.drawable.play_sound)
        val stopSoundDrawable = ContextCompat.getDrawable(context,R.drawable.stop_play)

        player?.let { player ->
            viewBinding.soundButtons.apply {
                player.setCompletionListener {
                    if (playSoundButton.icon.equals(stopSoundDrawable)) {
                        playSoundButton.icon = playSoundDrawable
                    }
                    player.reset()
                }

                playSoundButton.setOnClickListener {
                    if (player.isPlayingNow) {
                        player.stopPlay()
                        if (playSoundButton.icon.equals(stopSoundDrawable)) {
                            playSoundButton.icon = playSoundDrawable
                        }
                    } else {
                        player.normalPlaybackSpeed()
                        playSoundButton.icon = stopSoundDrawable
                        player.playSound(playerSource)
                    }
                }

                slowPlayButton.setOnClickListener {
                    if (player.isPlayingNow) {
                        player.stopPlay()
                    } else {
                        player.slowPlaybackSpeed()
                        player.playSound(playerSource)
                    }
                }
            }
        }
    }

    override fun clear() {
        _userAnswer = ""
        textField?.setText("")
        player = null
    }
}