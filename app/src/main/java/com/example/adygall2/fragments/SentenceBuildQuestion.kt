package com.example.adygall2.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.anton46.stepsview.StepsView
import com.example.adygall2.OnContinueBtnListener
import com.example.adygall2.R
import com.example.adygall2.adapters.SentenceAdapter
import com.example.adygall2.data.DataBaseSimulate

class SentenceBuildQuestion(
    private val listener: OnContinueBtnListener
) : Fragment() {

    private lateinit var wordsContainer : RecyclerView
    private lateinit var answerContainer : RecyclerView
    private lateinit var progressBar : StepsView
    private lateinit var continueBtn : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val view : View = inflater.inflate(R.layout.fragment_words_question, null)

        wordsContainer = view.findViewById(R.id.words_rec_view)
        answerContainer = view.findViewById(R.id.answer_rec_view)
        progressBar = view.findViewById(R.id.words_progress)
        continueBtn = view.findViewById(R.id.words_get_answer_button)

        continueBtn.setOnClickListener {
            listener.onClick()
        }

        customizeStepViewBar()

        val emptyAdapter = SentenceAdapter(emptyList())
        val fakeList = SentenceAdapter(DataBaseSimulate.fakeWords)

        answerContainer.adapter = emptyAdapter
        wordsContainer.adapter = fakeList


        return view
    }

    fun customizeStepViewBar() {
        val steps = Array(9) { "" }
        steps.fill(" ", 0, 8)
        progressBar.apply {
            labels = steps
            barColorIndicator = R.color.white
            progressColorIndicator = R.color.purple
            labelColorIndicator = R.color.purple
            completedPosition = 0
        }
    }
}