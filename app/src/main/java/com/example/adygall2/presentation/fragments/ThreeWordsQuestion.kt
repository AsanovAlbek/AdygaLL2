package com.example.adygall2.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.adygall2.R
import com.example.adygall2.data.db_models.Answer
import com.example.adygall2.databinding.FragmentThreeWordsQuestionBinding
import com.example.adygall2.presentation.GameViewModel
import com.example.adygall2.presentation.adapters.ThreeWordsAdapter
import com.example.adygall2.presentation.consts.ArgsKey
import com.example.adygall2.presentation.consts.ArgsKey.TASK_KEY
import org.koin.androidx.viewmodel.ext.android.viewModel

class ThreeWordsQuestion : Fragment(R.layout.fragment_three_words_question) {

    private lateinit var _binding : FragmentThreeWordsQuestionBinding
    private val binding get() = _binding
    private val viewModel by viewModel<GameViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(ArgsKey.MY_LOG_TAG, "Задание с тремя словами создано")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentThreeWordsQuestionBinding.inflate(inflater, container, false)

        val adapter = ThreeWordsAdapter(emptyList())
        binding.threeWordsRecView.adapter = adapter

        val fullTask = "Выберите слово ${arguments?.getString(TASK_KEY)}"
        binding.threeWordsTaskTv.text = fullTask

        setObservers()
        setAdapter(viewModel.answersListFromDb.value!!)

        return binding.root
    }

    private fun setObservers() {
        viewModel.getAnswers(
            arguments?.getInt(ArgsKey.ID_KEY)!!
        )
        viewModel.answersListFromDb.observe(viewLifecycleOwner, ::setAdapter)

    }

    private fun setAdapter(answers : List<Answer>) {

        val adapter = ThreeWordsAdapter(answers)
        binding.threeWordsRecView.adapter = adapter

    }
}