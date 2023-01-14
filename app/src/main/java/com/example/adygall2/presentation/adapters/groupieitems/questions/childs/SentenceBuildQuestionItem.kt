package com.example.adygall2.presentation.adapters.groupieitems.questions.childs

import android.content.Context
import android.view.View
import com.example.adygall2.R
import com.example.adygall2.data.models.SoundsPlayer
import com.example.adygall2.databinding.FragmentWordsQuestionBinding
import com.example.adygall2.domain.model.ComplexAnswer
import com.example.adygall2.domain.model.Source
import com.example.adygall2.presentation.adapters.SentenceAdapter
import com.example.adygall2.presentation.adapters.adapter_handles.AdapterHandleDragAndDropCallback
import com.example.adygall2.presentation.adapters.adapter_handles.HandleDragAndDropEvent
import com.example.adygall2.presentation.adapters.groupieitems.questions.parentitem.QuestionItem
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager

class SentenceBuildQuestionItem(
    private val context: Context,
    private val title: String,
    private val answers: List<ComplexAnswer>,
    private val soundsPlayer: SoundsPlayer,
    private val playerSource: Source
): QuestionItem<FragmentWordsQuestionBinding>(), AdapterHandleDragAndDropCallback {

    private lateinit var userAdapter: SentenceAdapter
    private lateinit var answerAdapter: SentenceAdapter
    private var _userAnswer = ""
    override val userAnswer: String get() = _userAnswer

    override val rightAnswer: String =
        answers.first().answer.correctAnswer

    override val onNextQuestion: () -> Unit
        get() = {
            _userAnswer = ""
            soundsPlayer.reset()
        }

    override fun bind(viewBinding: FragmentWordsQuestionBinding, position: Int) {
        viewBinding.apply {
            wordsTaskText.text = title
            bindButtons(this)
            bindAdapters(this)
        }
    }

    private fun bindButtons(viewBinding: FragmentWordsQuestionBinding) {
        viewBinding.apply {
            val playSoundDrawable = context.getDrawable(R.drawable.play_sound)
            val stopSoundDrawable = context.getDrawable(R.drawable.stop_play)
            soundButtons.apply {
                with(soundsPlayer) {
                    setCompletionListener {
                        playSoundButton.icon = playSoundDrawable
                        reset()
                    }

                    if (isPlayingNow) {
                        stopPlay()
                        playSoundButton.icon = playSoundDrawable
                    }

                    playSoundButton.setOnClickListener {
                        normalPlaybackSpeed()
                        if (isPlayingNow) {
                            playSoundButton.icon = playSoundDrawable
                        } else {
                            playSoundButton.icon = stopSoundDrawable
                        }
                        playSound(playerSource)
                    }

                    slowPlayButton.setOnClickListener {
                        slowPlaybackSpeed()
                        playSoundButton.icon = stopSoundDrawable
                        playSound(playerSource)
                    }
                }
            }
        }
    }

    private fun bindAdapters(viewBinding: FragmentWordsQuestionBinding) {
        val mutableAnswers = answers.map { it.answer.answer }.toMutableList()
        userAdapter = SentenceAdapter(
            context = context,
            isFirstAdapter = true,
            answers = mutableAnswers,
            callback = this
        )
        answerAdapter = SentenceAdapter(
            context = context,
            isFirstAdapter = false,
            answers = mutableListOf(),
            callback = this
        )
        val answerLayoutManager = FlexboxLayoutManager(context).apply {
            flexDirection = FlexDirection.ROW
        }

        viewBinding.apply {
            answerRecView.apply {
                layoutManager = answerLayoutManager
                adapter = answerAdapter
                setOnDragListener { _, dragEvent ->
                    HandleDragAndDropEvent(dragEvent).handle(
                        callback = this@SentenceBuildQuestionItem,
                        isFirstAdapter = false,
                        position = -1
                    )
                    true
                }
            }
            wordsRecView.apply {
                adapter = userAdapter
                setOnDragListener { _, dragEvent ->
                    HandleDragAndDropEvent(dragEvent).handle(
                        callback = this@SentenceBuildQuestionItem,
                        isFirstAdapter = true,
                        position = -1
                    )
                    true
                }
            }
        }
    }

    override fun getLayout(): Int = R.layout.fragment_words_question

    override fun initializeViewBinding(view: View): FragmentWordsQuestionBinding =
        FragmentWordsQuestionBinding.bind(view)

    override fun change(isFirstAdapter: Boolean, item: String, position: Int) {
        if (isFirstAdapter) {
            userAdapter.addAnswer(item, position)
            answerAdapter.removeAnswer(item)
        } else {
            answerAdapter.addAnswer(item, position)
            userAdapter.removeAnswer(item)
        }
        _userAnswer = answerAdapter.adapterItems.joinToString()
    }
}