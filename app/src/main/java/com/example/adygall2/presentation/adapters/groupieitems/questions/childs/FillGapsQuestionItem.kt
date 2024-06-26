package com.example.adygall2.presentation.adapters.groupieitems.questions.childs

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import com.example.adygall2.R
import com.example.adygall2.databinding.FragmentFillGapsBinding
import com.example.adygall2.domain.model.ComplexAnswer
import com.example.adygall2.presentation.adapters.groupieitems.questions.parentitem.QuestionItem
import com.google.android.flexbox.FlexboxLayout

class FillGapsQuestionItem(
    private val context: Context,
    private val title: String,
    private val answers: List<ComplexAnswer>
) : QuestionItem<FragmentFillGapsBinding>() {

    private var _userAnswer = mutableListOf<EditText>()
    private var textContainer: FlexboxLayout? = null
    override val userAnswer: String
        get() = _userAnswer.joinToString(
            separator = " ",
            postfix = "."
        ) { it.text }.replace("[1iLlI|]".toRegex(), "I")

    override val rightAnswer: String =
        answers.first().answer.answer.replace("[1iLlI|]".toRegex(), "I")

    override fun bind(viewBinding: FragmentFillGapsBinding, position: Int) {
        viewBinding.apply {
            textContainer = viewBinding.flexbox
            phraseTaskTv.text = answers.first().answer.correctAnswer
            setTaskText(viewBinding)
        }
    }

    private fun setTaskText(viewBinding: FragmentFillGapsBinding) {
        val textViews = title.split("****")
        textViews.forEachIndexed { index, item ->
            val addedTextView = TextView(context)
            addedTextView.setup(context, item)
            textContainer?.addView(addedTextView)
            if (index < textViews.size - 1) {
                val addedField = EditText(context)
                addedField.setup(context)
                textContainer?.addView(addedField)
                _userAnswer.add(addedField)
            }
        }
    }

    private fun TextView.setup(context: Context, textInView: String) = apply {
        text = textInView
        textSize = 18F
        maxLines = 1
        typeface = context.resources.getFont(R.font.pt_sans_bold)
        setBackgroundColor(context.getColor(R.color.gray_91))
        setPadding(0, 0, 10, 0)
        setTextColor(Color.BLACK)
    }

    private fun EditText.setup(context: Context) {
        width = 270
        textSize = 18F
        typeface = context.resources.getFont(R.font.pt_sans_bold)
        gravity = Gravity.CENTER
        setTextColor(Color.BLACK)
        setBackgroundResource(R.drawable.edit_text_rounded)
        setPadding(0, 0, 10, 0)
        isSingleLine = true
    }

    override fun getLayout(): Int = R.layout.fragment_fill_gaps

    override fun initializeViewBinding(view: View): FragmentFillGapsBinding =
        FragmentFillGapsBinding.bind(view)

    override fun clear() {
        _userAnswer.clear()
        textContainer?.removeAllViews()
    }
}