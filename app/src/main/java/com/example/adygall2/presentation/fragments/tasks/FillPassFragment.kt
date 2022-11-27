package com.example.adygall2.presentation.fragments.tasks

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.adygall2.R
import com.example.adygall2.domain.model.Answer
import com.example.adygall2.databinding.FragmentFillInThePassBinding
import com.example.adygall2.presentation.view_model.GameViewModel
import com.example.adygall2.presentation.adapters.SentenceAdapter
import com.example.adygall2.presentation.adapters.SimpleSentenceAdapter
import com.example.adygall2.presentation.consts.ArgsKey.ID_KEY
import com.example.adygall2.presentation.consts.ArgsKey.TASK_KEY
import com.example.adygall2.presentation.fragments.tasks.base_task.BaseTaskFragment
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel

/** Задание с заполнением ячейки словами из кнопок */
class FillPassFragment : BaseTaskFragment(R.layout.fragment_fill_in_the_pass) {

    private var _binding: FragmentFillInThePassBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<GameViewModel>()

    private lateinit var textViewFiled: TextView
    private var _userAnswer = ""
    override val userAnswer get() = _userAnswer
    private var _rightAnswer = ""
    override val rightAnswer get() = _rightAnswer

    private lateinit var userAdapter: SentenceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFillInThePassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        textViewFiled = TextView(requireActivity())
        setObservers()
        getDataFromViewModel()
    }

    private fun setObservers() {
        viewModel.answersListFromDb.observe(viewLifecycleOwner, ::setAdapter)
    }

    private fun getDataFromViewModel() {
        val taskId = arguments?.getInt(ID_KEY)
        viewModel.getAnswers(taskId!!)
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
            _userAnswer = viewModel.transform(str = textViewFiled.text.toString())
        }

        val rightAnswerStroke = answers.first { it.correctAnswer.toBoolean() }.answer
        _rightAnswer = viewModel.transform(rightAnswerStroke)
    }

    /** настройка параметров [TextView] */
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

    override fun onPause() {
        super.onPause()

        _userAnswer = ""
        _rightAnswer = ""
        textViewFiled.text = ""
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}