package com.example.adygall2.presentation.fragments.tasks

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.adygall2.R
import com.example.adygall2.data.db_models.Answer
import com.example.adygall2.databinding.FragmentFillGapsBinding
import com.example.adygall2.presentation.view_model.GameViewModel
import com.example.adygall2.presentation.consts.ArgsKey
import com.example.adygall2.presentation.consts.ArgsKey.TASK_KEY
import org.koin.androidx.viewmodel.ext.android.viewModel

class FillGapsFragment : Fragment(R.layout.fragment_fill_gaps) {

    private lateinit var _binding : FragmentFillGapsBinding
    private val binding get() = _binding
    private val viewModel by viewModel<GameViewModel>()

    private var _rightAnswer = ""
    val rightAnswer get() = _rightAnswer
    private var _userAnswer = mutableListOf<EditText>()
    val userAnswer get() = viewModel.transform(_userAnswer.joinToString { it.text })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(ArgsKey.MY_LOG_TAG, "Задание с заполнением пропусков создано")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFillGapsBinding.inflate(inflater, container, false)

        setObservers()
        setTaskText()

        return binding.root
    }

    private fun setObservers() {
        val taskId = arguments?.getInt(ArgsKey.ID_KEY)

        viewModel.getAnswers(taskId!!)
        viewModel.answersListFromDb.observe(viewLifecycleOwner) { answers ->
            val rightAnswerStroke = answers.first().answer
            _rightAnswer = viewModel.transform(rightAnswerStroke)
            binding.phraseTaskTv.text = answers.first().correctAnswer
        }
    }

    private fun setTaskText() {
        val userAnswerList = mutableListOf<EditText>()
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
                val addedFiled = EditText(requireActivity())
                addedFiled.apply {
                    width = 270
                    textSize = 18F
                    typeface = resources.getFont(R.font.pt_sans_bold)
                    setTextColor(Color.BLACK)
                    gravity = Gravity.CENTER
                    setBackgroundColor(Color.WHITE)
                    setPadding(0, 0, 10, 0)
                    isSingleLine = true
                }
                _userAnswer.add(addedFiled)
                binding.flexbox.addView(addedFiled)
            }
        }

    }
}