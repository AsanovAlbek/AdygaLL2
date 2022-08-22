package com.example.adygall2.presentation.fragments.tasks

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.adygall2.R
import com.example.adygall2.data.db_models.Answer
import com.example.adygall2.databinding.FragmentPairsOfWordsBinding
import com.example.adygall2.presentation.view_model.GameViewModel
import com.example.adygall2.presentation.adapters.PairsAdapter
import com.example.adygall2.presentation.adapters.StaticPairsAdapter
import com.example.adygall2.presentation.adapters.adapter_handles.AdapterCallback
import com.example.adygall2.presentation.adapters.adapter_handles.HandleDragAndDropEvent
import com.example.adygall2.presentation.consts.ArgsKey
import com.example.adygall2.presentation.consts.ArgsKey.TASK_KEY
import org.koin.androidx.viewmodel.ext.android.viewModel

class PairsOfWordsFragment : Fragment(R.layout.fragment_pairs_of_words), AdapterCallback {

    private lateinit var _pairsBinding : FragmentPairsOfWordsBinding
    private val pairsBinding get() = _pairsBinding
    private val viewModel by viewModel<GameViewModel>()
    private var _userAnswerPairsCheckList = mutableListOf<String>()
    val userAnswerPairsCheckList get() = _userAnswerPairsCheckList
    private var _rightAnswerPairsCheckList = listOf<String>()
    val rightAnswerPairsCheckList get() = _rightAnswerPairsCheckList
    private var _userAnswer = ""
    val userAnswer get() = _userAnswer
    private var _rightAnswer = ""
    val rightAnswer get() = _rightAnswer

    private lateinit var leftAdapter : StaticPairsAdapter
    private lateinit var rightAdapter : PairsAdapter
    private lateinit var bottomAdapter : PairsAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _pairsBinding = FragmentPairsOfWordsBinding.inflate(inflater, container, false)

        pairsBinding.taskText.text = arguments?.getString(TASK_KEY)

        setObservers()

        return pairsBinding.root
    }

    private fun setAdapters(answers : List<Answer>) {
        val answerPairs = mutableListOf<Pair<String, String>>()

        answers.forEach {
            val twoWords = it.answer.split("*")
            Log.i("Pairs", twoWords.toString())
            answerPairs.add( Pair( twoWords[0], twoWords[1]) )
        }

        val leftColumnWords = answerPairs.map { it.first }.toMutableList()
        val rightColumnWords = answerPairs.map { it.second }.toMutableList()

        leftAdapter = StaticPairsAdapter(leftColumnWords)
        rightAdapter = PairsAdapter(requireContext(),true, mutableListOf(), this)
        bottomAdapter = PairsAdapter(requireContext(),false, rightColumnWords, this)

        val bottomGridLayoutManager = GridLayoutManager(requireContext(), 2)
        pairsBinding.bottomWordsList.layoutManager = bottomGridLayoutManager

        pairsBinding.leftWordsList.adapter = leftAdapter
        pairsBinding.rightWordsList.adapter = rightAdapter
        pairsBinding.bottomWordsList.adapter = bottomAdapter

        pairsBinding.bottomWordsList.setOnDragListener { _, dragEvent ->
            HandleDragAndDropEvent(dragEvent).handle(this, false, -1)
            true
        }

        pairsBinding.rightWordsList.setOnDragListener { _, dragEvent ->
            HandleDragAndDropEvent(dragEvent).handle(this, true, -1)
            true
        }

        _userAnswerPairsCheckList = rightAdapter.adapterItems
        _rightAnswerPairsCheckList = answers.map { it.answer.split("*")[1] }
    }

    private fun setObservers() {
        val taskId = arguments?.getInt(ArgsKey.ID_KEY)

        viewModel.getAnswers(taskId!!)
        viewModel.answersListFromDb.observe(viewLifecycleOwner, ::setAdapters)
    }

    override fun change(isFirstAdapter: Boolean, item: String, position: Int) {
        if (isFirstAdapter) {
            bottomAdapter.removeAnswer(item)
            rightAdapter.addAnswer(item, position)
        }
        else {
            bottomAdapter.addAnswer(item, position)
            rightAdapter.removeAnswer(item)
        }
        _userAnswer = viewModel.transform(_userAnswerPairsCheckList.joinToString())
        _rightAnswer = viewModel.transform(_rightAnswerPairsCheckList.joinToString())
    }
}