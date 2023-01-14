package com.example.adygall2.presentation.adapters.groupieitems.questions.childs

import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import com.example.adygall2.R
import com.example.adygall2.databinding.FragmentTypeTranslateBinding
import com.example.adygall2.presentation.adapters.groupieitems.questions.parentitem.QuestionItem
import com.google.android.flexbox.FlexboxLayout

class TypeTranslateQuestionItem(
    private val context: Context,
    private val title: String,
    private val currentAnswer: String
): QuestionItem<FragmentTypeTranslateBinding>() {

    private var _userAnswer = ""
    override val userAnswer get() = _userAnswer

    override fun bind(viewBinding: FragmentTypeTranslateBinding, position: Int) {
        viewBinding.apply {
            userInputEt.addTextChangedListener {
                _userAnswer = it.toString()
            }
            taskTitleWithHints(wordsWithTooltip)
        }
    }

    override fun getLayout(): Int = R.layout.fragment_type_translate

    override fun initializeViewBinding(view: View): FragmentTypeTranslateBinding =
        FragmentTypeTranslateBinding.bind(view)

    private fun taskTitleWithHints(hintLayout: FlexboxLayout) {
        val hintsList = title.split(" ")
        hintsList.forEach {
            val addedWord = SpannableString(it)
            addedWord.setSpan(UnderlineSpan(), 0, addedWord.length, 0)
            val addedTextView = TextView(context)
            setTextViewParams(addedTextView, addedWord)
            hintLayout.addView(addedTextView)
        }
    }

    private fun setTextViewParams(textView: TextView, textInView: SpannableString) {
        textView.apply {
            text = textInView
            setTextColor(Color.BLACK)
            setPadding(10, 10, 10, 10)
            textSize = 18f
            setBackgroundColor(context.resources.getColor(R.color.gray_91, null))
        }
    }

    override val onNextQuestion: () -> Unit
        get() = {
            _userAnswer = ""
        }

    override val rightAnswer: String = currentAnswer

}