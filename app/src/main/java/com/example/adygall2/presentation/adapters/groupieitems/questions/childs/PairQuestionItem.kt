package com.example.adygall2.presentation.adapters.groupieitems.questions.childs

import android.content.Context
import android.view.View
import com.example.adygall2.R
import com.example.adygall2.databinding.FragmentPairsOfWordsBinding
import com.example.adygall2.domain.model.ComplexAnswer
import com.example.adygall2.presentation.adapters.PairsAdapter
import com.example.adygall2.presentation.adapters.StaticPairsAdapter
import com.example.adygall2.presentation.adapters.adapter_handles.AdapterHandleDragAndDropCallback
import com.example.adygall2.presentation.adapters.adapter_handles.HandleDragAndDropEvent
import com.example.adygall2.presentation.adapters.groupieitems.questions.parentitem.QuestionItem

class PairQuestionItem(
    private val context: Context,
    private val title: String,
    private val answers: List<ComplexAnswer>
): QuestionItem<FragmentPairsOfWordsBinding>(), AdapterHandleDragAndDropCallback {
    private var _userAnswer = ""
    override val userAnswer: String get() = _userAnswer
    private var _rightAnswer = ""
    private var userPairs = mutableListOf<String>()
    private var rightPairs = mutableListOf<String>()
    private lateinit var leftAdapter: StaticPairsAdapter
    private lateinit var rightAdapter: PairsAdapter
    private lateinit var bottomAdapter: PairsAdapter

    override val rightAnswer: String get() = _rightAnswer

    override val onNextQuestion: () -> Unit
        get() = {
            _userAnswer = ""
            userPairs.clear()
            rightPairs.clear()
        }

    override fun getLayout(): Int = R.layout.fragment_pairs_of_words

    override fun initializeViewBinding(view: View): FragmentPairsOfWordsBinding =
        FragmentPairsOfWordsBinding.bind(view)

    override fun change(isFirstAdapter: Boolean, item: String, position: Int) {
        if (isFirstAdapter) {
            bottomAdapter.removeAnswer(item)
            rightAdapter.addAnswer(item, position)
        } else {
            bottomAdapter.addAnswer(item, position)
            rightAdapter.removeAnswer(item)
        }
        _userAnswer = rightAdapter.adapterItems.joinToString()
        _rightAnswer = answers.joinToString { it.answer.answer.split("*")[1] }
    }

    override fun bind(viewBinding: FragmentPairsOfWordsBinding, position: Int) {
        setAdapters(viewBinding)
    }

    private fun setAdapters(viewBinding: FragmentPairsOfWordsBinding) {
        val answerPairs = mutableListOf<Pair<String, String>>()
        answers.forEach {
            val twoWords = it.answer.answer.split("*")
            answerPairs.add(Pair(twoWords[0], twoWords[1]))
        }

        val leftColumnWords = answerPairs.map { it.first }.toMutableList()
        val bottomColumnWords = answerPairs.map { it.second }.toMutableList()

        leftAdapter = StaticPairsAdapter(leftColumnWords)
        rightAdapter = PairsAdapter(
            context = context,
            isFirstAdapter = true,
            answers = mutableListOf(),
            callback = this
        )
        bottomAdapter = PairsAdapter(
            context = context,
            isFirstAdapter = false,
            answers = bottomColumnWords,
            callback = this
        )

        viewBinding.apply {

            bottomWordsList.apply {
                adapter = bottomAdapter
                setOnDragListener { _, dragEvent ->
                    HandleDragAndDropEvent(dragEvent).handle(
                        callback = this@PairQuestionItem,
                        isFirstAdapter = false,
                        position = -1
                    )
                    true
                }
            }

            rightWordsList.apply {
                adapter = rightAdapter
                setOnDragListener { _, dragEvent ->
                    HandleDragAndDropEvent(dragEvent).handle(
                        callback = this@PairQuestionItem,
                        isFirstAdapter = true,
                        position = -1
                    )
                    true
                }
            }

            leftWordsList.adapter = leftAdapter

            //userPairs = rightAdapter.adapterItems
            rightPairs = answers.map { it.answer.answer.split("*")[1] }.toMutableList()
        }
    }
}