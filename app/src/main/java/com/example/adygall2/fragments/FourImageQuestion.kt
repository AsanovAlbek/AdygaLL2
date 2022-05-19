package com.example.adygall2.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anton46.stepsview.StepsView
import com.example.adygall2.OnContinueBtnListener
import com.example.adygall2.R
import com.example.adygall2.adapters.ImageAdapter
import com.example.adygall2.data.DataBaseSimulate

class FourImageQuestion(
    private val listener : OnContinueBtnListener
) : Fragment() {
    private lateinit var levelNumberTv : TextView
    private lateinit var levelProgress : StepsView
    private lateinit var taskTv : TextView
    private lateinit var imageRecyclerView : RecyclerView
    private lateinit var getAnswerBtn : Button
    private lateinit var userHpBar : ProgressBar
    private lateinit var userExpBar : ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val view : View =
            inflater.inflate(R.layout.fragment_four_image_question, container, false)

        init(view)

        return view
    }

    fun customizeStepViewBar() {
        val steps = Array(9) { "" }
        steps.fill(" ", 0, 8)
        levelProgress.apply {
            labels = steps
            barColorIndicator = R.color.white
            progressColorIndicator = R.color.purple
            labelColorIndicator = R.color.purple
            completedPosition = 0
        }
    }

    fun init(view : View) {
        levelNumberTv = view.findViewById(R.id.image_que_level_number)
        levelProgress = view.findViewById(R.id.image_que_progress)
        taskTv = view.findViewById(R.id.image_que_task_text)
        imageRecyclerView = view.findViewById(R.id.image_que_rec_view)
        getAnswerBtn = view.findViewById(R.id.image_que_get_answer_button)
        userHpBar = view.findViewById(R.id.image_que_user_hp_bar)
        userExpBar = view.findViewById(R.id.image_que_user_exp_bar)

        imageRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        val adapter = ImageAdapter(DataBaseSimulate.ImageAnswerList)
        imageRecyclerView.adapter = adapter

        getAnswerBtn.setOnClickListener {
            listener.onClick()
        }

        customizeStepViewBar()
    }
}