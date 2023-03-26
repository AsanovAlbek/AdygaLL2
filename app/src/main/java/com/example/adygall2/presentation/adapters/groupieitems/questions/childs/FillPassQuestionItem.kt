package com.example.adygall2.presentation.adapters.groupieitems.questions.childs

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.example.adygall2.R
import com.example.adygall2.databinding.FragmentFillInThePassBinding
import com.example.adygall2.domain.model.ComplexAnswer
import com.example.adygall2.presentation.adapters.SimpleSentenceAdapter
import com.example.adygall2.presentation.adapters.groupieitems.questions.parentitem.QuestionItem
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.FlexboxLayoutManager

class FillPassQuestionItem(
    private val context: Context,
    private val title: String,
    private val answers: List<ComplexAnswer>
): QuestionItem<FragmentFillInThePassBinding>() {

    private var _userAnswer = ""
    override val userAnswer get() = _userAnswer
    private lateinit var userAdapter: SimpleSentenceAdapter
    private lateinit var textViewField: TextView
    private lateinit var textContainer: FlexboxLayout

    override val rightAnswer: String =
        answers.first { it.answer.correctAnswer.lowercase().toBoolean() }.answer.answer

    override val onNextQuestion: () -> Unit
        get() = {
            _userAnswer = ""
            textViewField.text = ""
            textContainer.removeAllViews()
        }

    override fun bind(viewBinding: FragmentFillInThePassBinding, position: Int) {
        val mutableList = answers.map { it.answer.answer }.toMutableList()
        textContainer = viewBinding.flexbox
        userAdapter = SimpleSentenceAdapter(mutableList)
        val manager = FlexboxLayoutManager(context).apply {
            flexDirection = FlexDirection.ROW
        }
        viewBinding.userBarRecycler.apply {
            adapter = userAdapter
            layoutManager = manager
        }

        val textViews = title.split("****")
        textViews.forEachIndexed { index, item ->
            val addedTextView = TextView(context)
            addedTextView.setup(context, item)
            textContainer.addView(addedTextView)

            if (index < textViews.size - 1) {
                val addedTextViewField = TextView(context)
                addedTextViewField.setupField(context)
                textViewField = addedTextViewField
                textContainer.addView(addedTextViewField)
            }
            setupUserAdapter(context)
        }
    }

    private fun setupUserAdapter(context: Context) {
        userAdapter.setListener {
            if (textViewField.text.isNotEmpty()) {
                val textInField = textViewField.text.toString()
                userAdapter.addAnswer(textInField)
            }
            textViewField.apply {
                text = it
                background = context.getDrawable(R.drawable.rounded_button2)
            }
            userAdapter.removeAnswer(it)
            _userAnswer = textViewField.text.toString()
        }
    }

    private fun TextView.setup(context: Context, textInView: String) = apply {
        text = textInView
        textSize = 18F
        typeface = context.resources.getFont(R.font.pt_sans_bold)
        setBackgroundColor(context.getColor(R.color.gray_91))
        setPadding(0, 0, 10, 0)
        setTextColor(Color.BLACK)
    }

    private fun TextView.setupField(context: Context) = apply {
        width = 270
        textSize = 18F
        gravity = Gravity.CENTER
        setBackgroundColor(Color.WHITE)
        setTextColor(Color.BLACK)
        typeface = context.resources.getFont(R.font.pt_sans_bold)
        setPadding(0, 0, 10, 0)
        setOnClickListener {
            if (text.isNotEmpty()) {
                userAdapter.addAnswer(text.toString())
                setBackgroundColor(Color.WHITE)
                text = ""
            }
        }
    }

    override fun getLayout(): Int = R.layout.fragment_fill_in_the_pass

    override fun initializeViewBinding(view: View): FragmentFillInThePassBinding =
        FragmentFillInThePassBinding.bind(view)
}