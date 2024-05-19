package com.example.adygall2.presentation.adapters.groupieitems.questions.childs

import android.content.Context
import android.media.MediaPlayer
import android.view.View
import androidx.core.content.ContextCompat
import com.example.adygall2.R
import com.example.adygall2.data.models.FilesHandler
import com.example.adygall2.data.models.SoundsPlayer
import com.example.adygall2.databinding.FragmentWordsQuestionBinding
import com.example.adygall2.domain.model.Answer
import com.example.adygall2.domain.model.ComplexAnswer
import com.example.adygall2.domain.model.Source
import com.example.adygall2.presentation.adapters.SentenceAdapter
import com.example.adygall2.presentation.adapters.adapter_handles.AdapterHandleDragAndDropCallback
import com.example.adygall2.presentation.adapters.adapter_handles.AdapterHandleDragAndDropCallbackWithAnswer
import com.example.adygall2.presentation.adapters.adapter_handles.HandleDragAndDropEvent
import com.example.adygall2.presentation.adapters.groupieitems.questions.parentitem.QuestionItem
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager

class SentenceBuildQuestionItem(
    private val context: Context,
    private val title: String,
    private val answers: List<ComplexAnswer>,
    private val playerSource: Source
) : QuestionItem<FragmentWordsQuestionBinding>(), AdapterHandleDragAndDropCallbackWithAnswer {

    private var userAdapter: SentenceAdapter? = null
    private var answerAdapter: SentenceAdapter? = null
    private var _userAnswer = ""
    override val userAnswer: String get() = _userAnswer.replace("[1iLlI|]".toRegex(), "I")
    private var player: SoundsPlayer? = null

    override val rightAnswer: String =
    answers.first().answer.correctAnswer.replace("[1iLlI|]".toRegex(), "I")

    override fun bind(viewBinding: FragmentWordsQuestionBinding, position: Int) {
        viewBinding.apply {
            wordsTaskText.text = title
            player = SoundsPlayer(context = context)
            bindButtons(this)
            bindAdapters(this)
        }
    }

    private fun bindButtons(viewBinding: FragmentWordsQuestionBinding) {
        viewBinding.apply {
            val playSoundDrawable = ContextCompat.getDrawable(context,R.drawable.play_sound)
            val stopSoundDrawable = ContextCompat.getDrawable(context,R.drawable.stop_play)

            soundButtons.apply {
                player?.let { player ->
                    player.setCompletionListener {
                        playSoundButton.icon = playSoundDrawable
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
    }

    private fun bindAdapters(viewBinding: FragmentWordsQuestionBinding) {
        val mutableAnswers = answers.map { it.answer }.toMutableList()
        userAdapter = SentenceAdapter(
            isFirstAdapter = true,
            answers = mutableAnswers,
            callback = this
        )
        answerAdapter = SentenceAdapter(
            isFirstAdapter = false,
            answers = mutableListOf(),
            callback = this
        )

        userAdapter?.clickAction = {
            userAdapter?.removeAnswer(it)
            answerAdapter?.addAnswer(it, -1)
            _userAnswer = answerAdapter?.adapterItems!!.joinToString(separator = " ", postfix = ".")
        }

        answerAdapter?.clickAction = {
            answerAdapter?.removeAnswer(it)
            userAdapter?.addAnswer(it, -1)
            _userAnswer = answerAdapter?.adapterItems!!.joinToString(separator = " ", postfix = ".")
        }

        val answerLayoutManager = FlexboxLayoutManager(context).apply {
            flexDirection = FlexDirection.ROW
        }

        viewBinding.apply {
            answerRecView.apply {
                layoutManager = answerLayoutManager
                adapter = answerAdapter
                setOnDragListener { _, dragEvent ->
                    HandleDragAndDropEvent(dragEvent).handleWithAnswer(
                        callbackWithAnswer = this@SentenceBuildQuestionItem,
                        isFirstAdapter = false,
                        position = -1
                    )
                    true
                }
            }
            wordsRecView.apply {
                adapter = userAdapter
                setOnDragListener { _, dragEvent ->
                    HandleDragAndDropEvent(dragEvent).handleWithAnswer(
                        callbackWithAnswer = this@SentenceBuildQuestionItem,
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

    override fun clear() {
        _userAnswer = ""
        player = null
    }

    override fun changeWithAnswer(isFirstAdapter: Boolean, item: Answer, position: Int) {
        if (isFirstAdapter) {
            userAdapter?.addAnswer(item, position)
            answerAdapter?.removeAnswer(item)
        } else {
            answerAdapter?.addAnswer(item, position)
            userAdapter?.removeAnswer(item)
        }
        _userAnswer = answerAdapter?.adapterItems!!.joinToString(separator = " ", postfix = ".")
    }
}