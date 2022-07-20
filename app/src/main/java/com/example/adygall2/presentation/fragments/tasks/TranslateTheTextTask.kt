package com.example.adygall2.presentation.fragments.tasks

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import com.example.adygall2.R
import com.example.adygall2.data.db_models.Answer
import com.example.adygall2.databinding.FragmentTranslateTheTextBinding
import com.example.adygall2.presentation.GameViewModel
import com.example.adygall2.presentation.adapters.SecondSentenceAdapter
import com.example.adygall2.presentation.adapters.SentenceAdapter
import com.example.adygall2.presentation.consts.ArgsKey.ID_KEY
import com.example.adygall2.presentation.consts.ArgsKey.TASK_KEY
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import java.util.regex.Pattern
import org.koin.androidx.viewmodel.ext.android.viewModel

class TranslateTheTextTask : Fragment(R.layout.fragment_translate_the_text) {

    private lateinit var _binding : FragmentTranslateTheTextBinding
    private val binding get() = _binding
    private val viewModel by viewModel<GameViewModel>()
    private var _userAnswer = listOf<Answer>()
    val userAnswer get() = _userAnswer
    private var _rightAnswer = String()
    val rightAnswer get() = _rightAnswer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTranslateTheTextBinding.inflate(inflater, container, false)
        setTaskText()
        setObservers()

        return binding.root
    }

    private fun setObservers() {
        val taskId = arguments?.getInt(ID_KEY)
        viewModel.getAnswers(taskId!!)
        viewModel.answersListFromDb.observe(viewLifecycleOwner, ::setAdapters)
    }

    private fun setAdapters(answers : List<Answer>) {
        val userAdapter = SentenceAdapter(answers)
        val answerAdapter = SecondSentenceAdapter(mutableListOf(), userAdapter)

        binding.answersRecycler.adapter = answerAdapter
        binding.userBarRecycler.adapter = userAdapter
        (binding.answersRecycler.layoutManager as FlexboxLayoutManager).flexDirection = FlexDirection.ROW
        (binding.userBarRecycler.layoutManager as FlexboxLayoutManager).flexDirection = FlexDirection.ROW

        _rightAnswer = answers.first().correctAnswer
        _userAnswer = answerAdapter.answerList
    }

    private fun setTaskText() {
        val taskText = arguments?.getString(TASK_KEY)
        taskText?.split(" ")?.forEach {
            val addedWord = SpannableString(it)
            addedWord.setSpan(UnderlineSpan(), 0, addedWord.length ,0)
            val addedTextView = TextView(requireActivity())
            setTextViewParams(addedTextView, addedWord)
            binding.wordsWithTooltip.addView(addedTextView)
        }
    }

    private fun setTextViewParams(textView : TextView, textInView : SpannableString) =
        textView.apply {
            text = textInView
            tooltipText = "Подсказка"
            setBackgroundColor(requireActivity().resources.getColor(R.color.gray_91, null))
            setPadding(10)
            textSize = 16F
            setTextColor(Color.BLACK)
        }
}