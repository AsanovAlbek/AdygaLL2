package com.example.adygall2.presentation.fragments.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adygall2.R
import com.example.adygall2.domain.model.Answer
import com.example.adygall2.databinding.FragmentThreeWordsQuestionBinding
import com.example.adygall2.presentation.view_model.GameViewModel
import com.example.adygall2.presentation.adapters.ThreeWordsAdapter
import com.example.adygall2.presentation.consts.ArgsKey
import com.example.adygall2.presentation.consts.ArgsKey.TASK_KEY
import com.example.adygall2.presentation.fragments.tasks.base_task.BaseTaskFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

/** Фрагмент для задания с тремя вариантами ответов */
class ThreeWordsFragment : BaseTaskFragment(R.layout.fragment_three_words_question) {

    private var _binding : FragmentThreeWordsQuestionBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<GameViewModel>()

    private var _userAnswer = ""
    override val userAnswer get() = _userAnswer
    private var _rightAnswer = ""
    override val rightAnswer get() = _rightAnswer



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentThreeWordsQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val fullTask = arguments?.getString(TASK_KEY)
        binding.threeWordsTaskTv.text = fullTask
        setObservers()
        viewModel.getAnswers(arguments?.getInt(ArgsKey.ID_KEY)!!)
    }

    private fun setObservers() {
        viewModel.answersListFromDb.observe(viewLifecycleOwner, ::setAdapter)
    }

    private fun setAdapter(answers : List<Answer>) {

        val adapter = ThreeWordsAdapter(requireContext(), answers) {
            _userAnswer = it.answer
        }
        binding.threeWordsRecView.adapter = adapter

        _rightAnswer = answers.first { it.correctAnswer.toBoolean() }.answer
    }

    override fun onPause() {
        super.onPause()
        _userAnswer = ""
        _rightAnswer = ""
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}