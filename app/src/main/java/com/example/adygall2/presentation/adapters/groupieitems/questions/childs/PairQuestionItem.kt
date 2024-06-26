package com.example.adygall2.presentation.adapters.groupieitems.questions.childs

import android.content.Context
import android.view.View
import com.example.adygall2.R
import com.example.adygall2.databinding.FragmentPairsOfWordsBinding
import com.example.adygall2.domain.model.Answer
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
) : QuestionItem<FragmentPairsOfWordsBinding>(), AdapterHandleDragAndDropCallback {
    private var _userAnswer = ""
    override val userAnswer: String get() = _userAnswer.replace("[1iLlI|]".toRegex(), "I")
    private var _rightAnswer = ""
    private var userPairs = mutableListOf<String>()
    private var rightPairs = mutableListOf<String>()
    private var leftAdapter: StaticPairsAdapter? = null
    private var rightAdapter: PairsAdapter? = null
    private var bottomAdapter: PairsAdapter? = null

    override val rightAnswer: String get() = _rightAnswer.replace("[1iLlI|]".toRegex(), "I")

    override fun getLayout(): Int = R.layout.fragment_pairs_of_words

    override fun initializeViewBinding(view: View): FragmentPairsOfWordsBinding =
        FragmentPairsOfWordsBinding.bind(view)

    override fun change(isFirstAdapter: Boolean, item: String, position: Int) {
        if (isFirstAdapter) {
            bottomAdapter?.removeAnswer(item)
            rightAdapter?.addAnswer(item, position)
        } else {
            bottomAdapter?.addAnswer(item, position)
            rightAdapter?.removeAnswer(item)
        }
        //_userAnswer = rightAdapter?.adapterItems!!.joinToString(separator = " ", postfix = ".")
        _userAnswer = leftAdapter!!.adapterItems.zip(rightAdapter?.adapterItems!!).joinToString { "${it.first}-${it.second}" }
        _rightAnswer = answers.joinToString(separator = " ", postfix = ".") { it.answer.answer.split("*")[1] }
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
        // Перемешанные слова
        val randomizedWords = bottomColumnWords.shuffled().toMutableList()

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
            answers = randomizedWords,
            callback = this
        )

        rightAdapter?.clickEvent = { word ->
            rightAdapter?.removeAnswer(word)
            bottomAdapter?.addAnswer(word, -1)
            _userAnswer = leftAdapter!!.adapterItems.zip(rightAdapter?.adapterItems!!).joinToString { "${it.first}-${it.second}" }
            _rightAnswer = answers.joinToString(separator = " ", postfix = ".") { it.answer.answer.replace("*", "-") }
        }
        bottomAdapter?.clickEvent = { word ->
            bottomAdapter?.removeAnswer(word)
            rightAdapter?.addAnswer(word, -1)
            _userAnswer = leftAdapter!!.adapterItems.zip(rightAdapter?.adapterItems!!).joinToString { "${it.first}-${it.second}" }
            _rightAnswer = answers.joinToString(separator = " ", postfix = ".") { it.answer.answer.replace("*", "-") }
        }

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

    override fun clear() {
        _userAnswer = ""
        userPairs.clear()
        rightPairs.clear()
    }
}