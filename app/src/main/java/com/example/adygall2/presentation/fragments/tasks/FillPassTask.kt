package com.example.adygall2.presentation.fragments.tasks

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.adygall2.R
import com.example.adygall2.data.db_models.Answer
import com.example.adygall2.databinding.FragmentFillInThePassBinding
import com.example.adygall2.presentation.view_model.GameViewModel
import com.example.adygall2.presentation.adapters.SentenceAdapter
import com.example.adygall2.presentation.adapters.SimpleSentenceAdapter
import com.example.adygall2.presentation.consts.ArgsKey.ID_KEY
import com.example.adygall2.presentation.consts.ArgsKey.TASK_KEY
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel

class FillPassTask : Fragment(R.layout.fragment_fill_in_the_pass) {

    private lateinit var _binding: FragmentFillInThePassBinding
    private val binding get() = _binding
    private val viewModel by viewModel<GameViewModel>()

    private lateinit var textViewFiled: TextView
    private var _userAnswer = ""
    val userAnswer get() = _userAnswer
    private var _rightAnswer = ""
    val rightAnswer get() = _rightAnswer

    private lateinit var userAdapter: SentenceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFillInThePassBinding.inflate(inflater, container, false)

        textViewFiled = TextView(requireActivity())
        setObservers()

        return binding.root
    }

    private fun setObservers() {
        val taskId = arguments?.getInt(ID_KEY)

        viewModel.getAnswers(taskId!!)
        viewModel.answersListFromDb.observe(viewLifecycleOwner, ::setAdapter)
    }

    private fun setAdapter(answers: MutableList<Answer>) {
        val mutableList = answers.map { it.answer }.toMutableList()
        val userAdapter = SimpleSentenceAdapter(mutableList)
        binding.userBarRecycler.adapter = userAdapter
        val layoutManager = FlexboxLayoutManager(requireActivity())
        layoutManager.flexDirection = FlexDirection.ROW
        binding.userBarRecycler.layoutManager = layoutManager

        val task = arguments?.getString(TASK_KEY)
        val textViews = task?.split("****")
        textViews?.forEachIndexed { index, it ->
            val addedTextView = TextView(requireActivity())
            addedTextView.apply {
                text = it
                textSize = 18F
                typeface = resources.getFont(R.font.pt_sans_bold)
                setBackgroundColor(requireActivity().resources.getColor(R.color.gray_91, null))
                setPadding(0, 0, 10, 0)
                setTextColor(Color.BLACK)
            }
            binding.flexbox.addView(addedTextView)

            if (index < textViews.size - 1) {
                val addedTextViewFiled = TextView(requireActivity())
                addedTextViewFiled.apply {
                    width = 270
                    textSize = 18F
                    gravity = Gravity.CENTER
                    setBackgroundColor(Color.WHITE)
                    setTextColor(Color.BLACK)
                    typeface = resources.getFont(R.font.pt_sans_bold)
                    setPadding(0, 0, 10, 0)
                }
                addedTextViewFiled.setOnClickListener {
                    addedTextViewFiled.apply {
                        if (addedTextViewFiled.text.isNotEmpty()) {
                            userAdapter.addAnswer(addedTextViewFiled.text.toString())
                            setBackgroundColor(Color.WHITE)
                            text = ""
                        }
                    }
                }
                textViewFiled = addedTextViewFiled
                binding.flexbox.addView(addedTextViewFiled)
            }
        }

        userAdapter.setListener {
            if (textViewFiled.text.isNotEmpty()) {
                val textInFiled = textViewFiled.text.toString()
                userAdapter.addAnswer(textInFiled)
            }
            textViewFiled.text = it
            textViewFiled.background =
                resources.getDrawable(R.drawable.rounded_button2, null)
            userAdapter.removeAnswer(it)
            _userAnswer = viewModel.transform(textViewFiled.text.toString())
        }

        val rightAnswerStroke = answers.first { it.correctAnswer.toBoolean() }.answer
        _rightAnswer = viewModel.transform(rightAnswerStroke)
    }

    private fun setTextViewParams(textView: TextView, textInView: String) {
        textView.apply {
            text = textInView
            textSize = 18F
            typeface = resources.getFont(R.font.pt_sans_bold)
            setBackgroundColor(requireActivity().resources.getColor(R.color.gray_91, null))
            setPadding(0, 0, 10, 0)
            setTextColor(Color.BLACK)
        }
    }
}