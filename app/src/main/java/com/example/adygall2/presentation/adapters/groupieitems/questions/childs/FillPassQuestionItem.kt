package com.example.adygall2.presentation.adapters.groupieitems.questions.childs

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBar.LayoutParams
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.allViews
import androidx.core.view.contains
import androidx.core.view.marginRight
import com.example.adygall2.R
import com.example.adygall2.databinding.FragmentFillInThePassBinding
import com.example.adygall2.domain.model.ComplexAnswer
import com.example.adygall2.presentation.adapters.SimpleSentenceAdapter
import com.example.adygall2.presentation.adapters.groupieitems.questions.parentitem.QuestionItem
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel

class FillPassQuestionItem(
    private val context: Context,
    private val title: String,
    private val answers: List<ComplexAnswer>
) : QuestionItem<FragmentFillInThePassBinding>() {

    private var _userAnswer = ""
    override val userAnswer get() = _userAnswer.replace("[1iLlI|]".toRegex(), "I")
    private var userAdapter: SimpleSentenceAdapter? = null
    private var textViewField: TextView? = null
    private var textContainer: FlexboxLayout? = null

    override val rightAnswer: String =
        answers.first {
            it.answer.correctAnswer.lowercase().toBoolean()
        }.answer.answer.replace("[1iLlI|]".toRegex(), "I")

    override fun bind(viewBinding: FragmentFillInThePassBinding, position: Int) {
        val mutableList = answers.map { it.answer.answer }.toMutableList()
        textContainer = viewBinding.flexbox
        textContainer?.removeAllViews()
        Log.d(
            "GAME",
            "text container before ${textContainer?.allViews?.joinToString { it::class.toString() }}"
        )
        userAdapter = SimpleSentenceAdapter(mutableList)
        val manager = FlexboxLayoutManager(context).apply {
            flexDirection = FlexDirection.ROW
        }
        viewBinding.userBarRecycler.apply {
            adapter = userAdapter
            layoutManager = manager
        }

        val textViews = title.split("****")
        Log.d("GAME", "Pass items ${textViews.joinToString(separator = "|")}")
        textViews.forEachIndexed { index, item ->
            val addedTextView = TextView(context)
            addedTextView.setup(context, item)
            textContainer?.addView(addedTextView)

            if (index < textViews.size - 1) {
                val addedTextViewField = TextView(context)
                addedTextViewField.setupField(context)
                textViewField = addedTextViewField
                textContainer?.addView(addedTextViewField)
            }
            setupUserAdapter(context)
        }
        Log.d(
            "GAME",
            "text container ${textContainer?.allViews?.joinToString { if (it is TextView) it.text else "not text view" }}"
        )
    }

    private fun setupUserAdapter(context: Context) {
        userAdapter?.setListener { word ->
            if (textViewField?.text!!.isNotEmpty()) {
                val textInField = textViewField?.text.toString()
                userAdapter?.addAnswer(textInField)
            }
            textViewField.apply {
                this?.let {
                    text = word
                    //background = ContextCompat.getDrawable(context, R.drawable.pair_words_slot)
                    //setBackgroundColor(Color.WHITE)
                }

            }
            userAdapter?.removeAnswer(word)
            _userAnswer = textViewField?.text.toString()
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

    private fun TextView.setupField(context: Context) = apply {
        val layoutParams =
            FlexboxLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                setMargins(0, 0, 10, 0)
            }
        setLayoutParams(layoutParams)
        width = 270
        textSize = 18F
        maxLines = 1
        gravity = Gravity.CENTER
        setBackgroundColor(Color.WHITE)
        setTextColor(Color.BLACK)
        typeface = context.resources.getFont(R.font.pt_sans_bold)
        setPadding(0, 0, 10, 0)
        setOnClickListener {
            if (text.isNotEmpty()) {
                userAdapter?.addAnswer(text.toString())
                setBackgroundColor(Color.WHITE)
                text = ""
            }
        }
    }

    override fun getLayout(): Int = R.layout.fragment_fill_in_the_pass

    override fun initializeViewBinding(view: View): FragmentFillInThePassBinding =
        FragmentFillInThePassBinding.bind(view)

    override fun clear() {
        _userAnswer = ""
        textViewField?.text = ""
        textContainer?.removeAllViews()
    }
}