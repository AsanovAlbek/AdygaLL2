package com.example.adygall2.presentation.adapters.groupieitems.questions.childs

import android.content.Context
import android.view.View
import com.example.adygall2.R
import com.example.adygall2.databinding.FragmentThreeWordsQuestionBinding
import com.example.adygall2.domain.model.ComplexAnswer
import com.example.adygall2.presentation.adapters.ThreeWordsAdapter
import com.example.adygall2.presentation.adapters.groupieitems.questions.parentitem.QuestionItem

class ThreeWordsQuestionItem(
    private val context: Context,
    private val title: String,
    private val answers: List<ComplexAnswer>
) : QuestionItem<FragmentThreeWordsQuestionBinding>() {

    private var _userAnswer = ""
    override val userAnswer get() = _userAnswer.replace("[1iLlI|]".toRegex(), "I")

    override fun bind(viewBinding: FragmentThreeWordsQuestionBinding, position: Int) {
        viewBinding.apply {
            threeWordsRecView.adapter = ThreeWordsAdapter(
                context = context,
                itemsList = answers
            ) {
                _userAnswer = it.answer.answer
            }

            threeWordsTaskTv.text = title
        }
    }

    override fun getLayout(): Int = R.layout.fragment_three_words_question

    override fun initializeViewBinding(view: View): FragmentThreeWordsQuestionBinding =
        FragmentThreeWordsQuestionBinding.bind(view)


    override val rightAnswer: String =
        answers.map { it.answer }.first { it.correctAnswer.lowercase().toBoolean() }.answer.replace("[1iLlI|]".toRegex(), "I")

    override fun clear() {
        _userAnswer = ""
    }
}