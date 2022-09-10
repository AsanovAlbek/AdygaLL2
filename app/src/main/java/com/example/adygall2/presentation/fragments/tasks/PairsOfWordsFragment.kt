package com.example.adygall2.presentation.fragments.tasks

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.adygall2.R
import com.example.adygall2.domain.model.Answer
import com.example.adygall2.databinding.FragmentPairsOfWordsBinding
import com.example.adygall2.presentation.view_model.GameViewModel
import com.example.adygall2.presentation.adapters.PairsAdapter
import com.example.adygall2.presentation.adapters.StaticPairsAdapter
import com.example.adygall2.presentation.adapters.adapter_handles.AdapterHandleDragAndDropCallback
import com.example.adygall2.presentation.adapters.adapter_handles.HandleDragAndDropEvent
import com.example.adygall2.presentation.consts.ArgsKey
import com.example.adygall2.presentation.consts.ArgsKey.TASK_KEY
import com.example.adygall2.presentation.fragments.tasks.base_task.BaseTaskFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Фрагмент для заданий с парами ответов
 */
class PairsOfWordsFragment : BaseTaskFragment(R.layout.fragment_pairs_of_words), AdapterHandleDragAndDropCallback {

    private lateinit var _pairsBinding : FragmentPairsOfWordsBinding
    private val pairsBinding get() = _pairsBinding
    private val viewModel by viewModel<GameViewModel>()
    private var _userAnswerPairsCheckList = mutableListOf<String>()
    val userAnswerPairsCheckList get() = _userAnswerPairsCheckList
    private var _rightAnswerPairsCheckList = listOf<String>()
    val rightAnswerPairsCheckList get() = _rightAnswerPairsCheckList
    private var _userAnswer = ""
    override val userAnswer get() = _userAnswer
    private var _rightAnswer = ""
    override val rightAnswer get() = _rightAnswer

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
        getDataFromViewModel()

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

        val bottomGridLayoutManager = GridLayoutManager(requireContext(), 2)
        pairsBinding.bottomWordsList.layoutManager = bottomGridLayoutManager

        // Один адаптер статичен
        leftAdapter = StaticPairsAdapter(leftColumnWords)
        rightAdapter = PairsAdapter(requireContext(),true, mutableListOf(), this)
        bottomAdapter = PairsAdapter(requireContext(),false, rightColumnWords, this)

        pairsBinding.leftWordsList.adapter = leftAdapter
        pairsBinding.rightWordsList.adapter = rightAdapter
        pairsBinding.bottomWordsList.adapter = bottomAdapter

        // Перетаскивание
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
        viewModel.answersListFromDb.observe(viewLifecycleOwner, ::setAdapters)
    }

    private fun getDataFromViewModel() {
        val taskId = arguments?.getInt(ArgsKey.ID_KEY)
        viewModel.getAnswers(taskId!!)
    }

    /** Метод для изменения содержимого адаптеров */
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