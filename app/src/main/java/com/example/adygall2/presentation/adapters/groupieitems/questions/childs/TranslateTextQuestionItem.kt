package com.example.adygall2.presentation.adapters.groupieitems.questions.childs

import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.TextView
import com.example.adygall2.R
import com.example.adygall2.databinding.FragmentTranslateTheTextBinding
import com.example.adygall2.domain.model.Answer
import com.example.adygall2.domain.model.ComplexAnswer
import com.example.adygall2.presentation.adapters.SentenceAdapter
import com.example.adygall2.presentation.adapters.adapter_handles.AdapterHandleDragAndDropCallback
import com.example.adygall2.presentation.adapters.adapter_handles.AdapterHandleDragAndDropCallbackWithAnswer
import com.example.adygall2.presentation.adapters.adapter_handles.HandleDragAndDropEvent
import com.example.adygall2.presentation.adapters.groupieitems.questions.parentitem.QuestionItem
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.FlexboxLayoutManager
import com.tomergoldst.tooltips.ToolTip
import com.tomergoldst.tooltips.ToolTipsManager

class TranslateTextQuestionItem(
    private val context: Context,
    private val title: String,
    private val answers: List<ComplexAnswer>
) : QuestionItem<FragmentTranslateTheTextBinding>(), AdapterHandleDragAndDropCallbackWithAnswer {
    private var _userAnswer = ""
    override val userAnswer: String get() = _userAnswer.replace("[1iLlI|]".toRegex(), "I")
    private var userAdapter: SentenceAdapter? = null
    private var answerAdapter: SentenceAdapter? = null
    private var tooltipBar: FlexboxLayout? = null
    private val tooltipManagers = mutableListOf<ToolTipsManager>()

    override val rightAnswer: String = answers.first().answer.correctAnswer.replace("[1iLlI|]".toRegex(), "I")

    override fun getLayout(): Int = R.layout.fragment_translate_the_text

    override fun initializeViewBinding(view: View): FragmentTranslateTheTextBinding =
        FragmentTranslateTheTextBinding.bind(view)

    override fun bind(viewBinding: FragmentTranslateTheTextBinding, position: Int) {
        setAdapters(viewBinding)
        setTaskText(viewBinding)
    }

    override fun changeWithAnswer(isFirstAdapter: Boolean, item: Answer, position: Int) {
        if (isFirstAdapter) {
            userAdapter?.addAnswer(item, position)
            answerAdapter?.removeAnswer(item)
        } else {
            userAdapter?.removeAnswer(item)
            answerAdapter?.addAnswer(item, position)
        }
        _userAnswer = answerAdapter?.adapterItems!!.joinToString(separator = " ", postfix = ".")
    }

    private fun setAdapters(viewBinding: FragmentTranslateTheTextBinding) {
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

        answerAdapter?.clickAction = {
            answerAdapter?.removeAnswer(it)
            userAdapter?.addAnswer(it, -1)
            _userAnswer = answerAdapter?.adapterItems!!.joinToString(separator = " ", postfix = ".")
        }

        userAdapter?.clickAction = {
            userAdapter?.removeAnswer(it)
            answerAdapter?.addAnswer(it, -1)
            _userAnswer = answerAdapter?.adapterItems!!.joinToString(separator = " ", postfix = ".")

        }

        viewBinding.apply {
            answersRecycler.apply {
                adapter = answerAdapter
                setOnDragListener { _, dragEvent ->
                    HandleDragAndDropEvent(dragEvent).handleWithAnswer(
                        callbackWithAnswer = this@TranslateTextQuestionItem,
                        isFirstAdapter = false,
                        position = -1
                    )
                    true
                }
                (layoutManager as FlexboxLayoutManager).flexDirection = FlexDirection.ROW
            }

            userBarRecycler.apply {
                adapter = userAdapter
                setOnDragListener { _, dragEvent ->
                    HandleDragAndDropEvent(dragEvent).handleWithAnswer(
                        callbackWithAnswer = this@TranslateTextQuestionItem,
                        isFirstAdapter = true,
                        position = -1
                    )
                    true
                }
                (layoutManager as FlexboxLayoutManager).flexDirection = FlexDirection.ROW
            }
        }
    }

    private fun setTaskText(viewBinding: FragmentTranslateTheTextBinding) {
        tooltipBar = viewBinding.wordsWithTooltip
        tooltipManagers.forEach { it.dismissAll() }

        val taskParts = title.split("*")
        val hints = taskParts[1].split("#")
        taskParts.first().split("#").forEachIndexed { index, item ->
            val addedWord = SpannableString(item)
            addedWord.setSpan(UnderlineSpan(), 0, item.length, 0)
            val addedTextView = TextView(context)
            addedTextView.setup(context, addedWord)
            val toolTipsManager = ToolTipsManager()
            toolTipsManager.findAndDismiss(addedTextView)
            val tooltipBuilder = setTooltip(viewBinding, addedTextView, hints[index])
            tooltipManagers.add(toolTipsManager)
            addedTextView.setOnClickListener {
                tooltipManagers.forEach { it.dismissAll() }
                //toolTipsManager.show(tooltipBuilder.build())
                tooltipManagers.find { it == toolTipsManager }?.show(tooltipBuilder.build())
            }
            tooltipBar?.addView(addedTextView)
        }
    }

    private fun setTooltip(
        viewBinding: FragmentTranslateTheTextBinding,
        tv: TextView,
        hintWord: String
    ): ToolTip.Builder {
        val builder = ToolTip.Builder(
            context,
            tv, viewBinding.rootForTooltip,
            hintWord, ToolTip.POSITION_ABOVE
        )

        builder.apply {
            setAlign(ToolTip.ALIGN_CENTER)
            setBackgroundColor(context.getColor(R.color.persian_orange))
        }

        return builder
    }

    private fun TextView.setup(context: Context, word: SpannableString) = apply {
        text = word
        maxLines = 1
        setBackgroundColor(context.getColor(R.color.gray_91))
        setPadding(0, 0, 10, 0)
        textSize = 18F
        setTextColor(Color.BLACK)
    }

    override fun clear() {
        _userAnswer = ""
        tooltipManagers.forEach { it.dismissAll() }
        tooltipBar?.removeAllViews()
        tooltipManagers.clear()
        tooltipBar?.invalidate()
        tooltipBar = null
    }
}