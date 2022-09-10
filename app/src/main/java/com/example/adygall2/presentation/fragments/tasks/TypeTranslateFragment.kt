package com.example.adygall2.presentation.fragments.tasks

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.adygall2.R
import com.example.adygall2.databinding.FragmentTypeTranslateBinding
import com.example.adygall2.domain.model.Answer
import com.example.adygall2.presentation.consts.ArgsKey
import com.example.adygall2.presentation.consts.ArgsKey.ID_KEY
import com.example.adygall2.presentation.fragments.tasks.base_task.BaseTaskFragment
import com.example.adygall2.presentation.view_model.GameViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
/** Фрагмент для задания с  переводом текста через клавиатуру*/
class TypeTranslateFragment : BaseTaskFragment(R.layout.fragment_type_translate){

    private lateinit var _typeTranslateBinding : FragmentTypeTranslateBinding
    private val typeTranslateBinding get() = _typeTranslateBinding
    private val viewModel by viewModel<GameViewModel>()

    private var _rightAnswer = ""
    override val rightAnswer get() = _rightAnswer
    private var _userAnswer = ""
    override val userAnswer get() = _userAnswer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _typeTranslateBinding = FragmentTypeTranslateBinding.bind(view)
        setTaskText()
        setObservers()
        getDataFromViewModel()
        editTextChangeListener()
    }

    private fun setObservers() {
        viewModel.answersListFromDb.observe(viewLifecycleOwner, ::setupAnswers)
    }

    private fun setupAnswers(answers : List<Answer>) {
        val answersStroke = answers.first().correctAnswer
        _rightAnswer = viewModel.transform(answersStroke)
    }

    private fun getDataFromViewModel() {
        val taskId = arguments?.getInt(ID_KEY)
        viewModel.getAnswers(taskId!!)
    }

    private fun setTaskText() {
        // Получаем текст задания
        val taskText = arguments?.getString(ArgsKey.TASK_KEY)
        // Вторую часть преобразуем в лист строк, разделённых #
        val hintsForTaskText = taskText?.split(" ")
        taskText?.split(" ")?.forEachIndexed { _, it ->
            // Добавляемое подчёркнутое слово
            val addedWord = SpannableString(it)
            // Установка подчёркивания
            addedWord.setSpan(UnderlineSpan(), 0, addedWord.length, 0)
            val addedTextView = TextView(requireActivity())
            setTextViewParams(addedTextView, addedWord)
            typeTranslateBinding.wordsWithTooltip.addView(addedTextView)
        }
    }

    /** Слушатель изменения текста в текстовом поле */
    private fun editTextChangeListener() {
        typeTranslateBinding.userInputEt.addTextChangedListener {
            val userAnswerStroke = it.toString()
            _userAnswer = viewModel.transform(userAnswerStroke)
        }
    }

    /** Изменение параметров [textView] */
    private fun setTextViewParams(textView: TextView, textInView: SpannableString) =
        textView.apply {
            text = textInView
            setBackgroundColor(requireActivity().resources.getColor(R.color.gray_91, null))
            setPadding(10)
            textSize = 18F
            setTextColor(Color.BLACK)
        }
}