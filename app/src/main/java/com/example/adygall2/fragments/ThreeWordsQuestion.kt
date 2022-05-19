package com.example.adygall2.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.anton46.stepsview.StepsView
import com.example.adygall2.OnContinueBtnListener
import com.example.adygall2.R
import com.example.adygall2.adapters.ThreeWordsAdapter
import com.example.adygall2.data.DataBaseSimulate

class ThreeWordsQuestion(
    private val listener: OnContinueBtnListener
) : Fragment() {

    private lateinit var progressBar : StepsView
    private lateinit var continueButton : Button
    private lateinit var wordListRv : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_three_words_question, null)

        progressBar = view.findViewById(R.id.three_words_progress)
        continueButton = view.findViewById(R.id.three_words_get_answer_button)

        customizeStepViewBar()

        wordListRv = view.findViewById(R.id.three_words_rec_view)

        val adapter = ThreeWordsAdapter(DataBaseSimulate.simpleWordsAnswerList)
        wordListRv.adapter = adapter

        continueButton.setOnClickListener { listener.onClick() }
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