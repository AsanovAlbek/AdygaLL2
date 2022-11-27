package com.example.adygall2.presentation.fragments.tasks

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adygall2.R
import com.example.adygall2.databinding.FragmentPairsOfWordsBinding
import com.example.adygall2.domain.model.Answer
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
class PairsOfWordsFragment : BaseTaskFragment(R.layout.fragment_pairs_of_words)
    , AdapterHandleDragAndDropCallback {

    private var _pairsBinding : FragmentPairsOfWordsBinding? = null
    private val pairsBinding get() = _pairsBinding!!
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
        return pairsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        pairsBinding.taskText.text = arguments?.getString(TASK_KEY)
        setObservers()
        getDataFromViewModel()
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

        // Один адаптер статичен
        leftAdapter = StaticPairsAdapter(leftColumnWords)
        rightAdapter = PairsAdapter(requireContext(),true, mutableListOf(), this)
        bottomAdapter = PairsAdapter(requireContext(),false, rightColumnWords, this)

        pairsBinding.apply {
            leftWordsList.adapter = leftAdapter
            rightWordsList.adapter = rightAdapter
            bottomWordsList.adapter = bottomAdapter
        }

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

    override fun onPause() {
        super.onPause()
        _userAnswer = ""
        _rightAnswer = ""
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _pairsBinding = null
    }
}