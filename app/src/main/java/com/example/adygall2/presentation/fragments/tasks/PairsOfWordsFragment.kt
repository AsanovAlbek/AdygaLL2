package com.example.adygall2.presentation.fragments.tasks

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.adygall2.R
import com.example.adygall2.data.db_models.Answer
import com.example.adygall2.databinding.FragmentPairsOfWordsBinding
import com.example.adygall2.presentation.GameViewModel
import com.example.adygall2.presentation.adapters.PairsAdapter
import com.example.adygall2.presentation.consts.ArgsKey
import com.example.adygall2.presentation.consts.ArgsKey.TASK_KEY
import org.koin.androidx.viewmodel.ext.android.viewModel

class PairsOfWordsFragment : Fragment(R.layout.fragment_pairs_of_words) {

    private lateinit var _binding : FragmentPairsOfWordsBinding
    private val binding get() = _binding
    private val viewModel by viewModel<GameViewModel>()
    private var _userAnswerPairsCheckList = mutableListOf<String>()
    val userAnswerPairsCheckList get() = _userAnswerPairsCheckList
    private var _rightAnswerPairsCheckList = listOf<String>()
    val rightAnswerPairsCheckList get() = _rightAnswerPairsCheckList

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPairsOfWordsBinding.inflate(inflater, container, false)

        binding.taskText.text = arguments?.getString(TASK_KEY)

        setObservers()

        return binding.root
    }

    private fun setAdapters(answers : List<Answer>) {
        val answerPairs = mutableListOf<Pair<String, String>>()

        answers.forEach {
            val twoWords = it.answer.split("*")
            Log.i("Pairs", twoWords.toString())
            answerPairs.add( Pair( twoWords[0], twoWords[1]) )
        }

        var leftIndex = 0
        var rightIndex = 0

        val leftColumnWords = answerPairs.map { it.first }.toMutableList()
        val rightColumnWords = answerPairs.map { it.second }.toMutableList()
        val leftAdapter = PairsAdapter(leftColumnWords)
        val rightAdapter = PairsAdapter(rightColumnWords)

        binding.leftWordsList.adapter = leftAdapter
        binding.rightWordsList.adapter = rightAdapter

        leftAdapter.onItemClick { word ->
            if (leftIndex < rightIndex) {
                _userAnswerPairsCheckList[leftIndex] = "$word*${_userAnswerPairsCheckList[leftIndex]}"
            }
            else {
                _userAnswerPairsCheckList.add(leftIndex, "$word*")
            }
            leftIndex++
        }

        rightAdapter.onItemClick { word ->
            if (rightIndex < leftIndex) {
                _userAnswerPairsCheckList[rightIndex] += word
            }
            else {
                _userAnswerPairsCheckList.add(rightIndex, word)
            }

            rightIndex++
        }

        _rightAnswerPairsCheckList = answers.map { it.answer }
    }

    private fun setObservers() {
        val taskId = arguments?.getInt(ArgsKey.ID_KEY)

        viewModel.getAnswers(taskId!!)
        viewModel.answersListFromDb.observe(viewLifecycleOwner, ::setAdapters)
    }
}