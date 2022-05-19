package com.example.adygall2.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.anton46.stepsview.StepsView
import com.example.adygall2.OnContinueBtnListener
import com.example.adygall2.R
import de.hdodenhof.circleimageview.CircleImageView

class PhraseFragment(
    private val listener : OnContinueBtnListener
) : Fragment() {

    lateinit var closeBtn : ImageButton
    lateinit var levelNumberTv : TextView
    lateinit var stepBar : StepsView
    lateinit var taskTv: TextView
    lateinit var questionTv : TextView
    lateinit var answerTv : TextView
    lateinit var missingAnswerPartEt : EditText
    lateinit var continueBtn : Button
    lateinit var avatar : CircleImageView
    lateinit var hpBar : ProgressBar
    lateinit var expBar : ProgressBar
    lateinit var userNameTv : TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view : View = inflater.inflate(R.layout.fragment_phrase_complete, null)

        init(view)

        return view
    }

    fun customizeStepViewBar() {
        val steps = Array(9) { "" }
        steps.fill(" ", 0, 8)
        stepBar.apply {
            labels = steps
            barColorIndicator = R.color.white
            progressColorIndicator = R.color.purple
            labelColorIndicator = R.color.purple
            completedPosition = 0
        }
    }

    fun init(view : View) {
        closeBtn = view.findViewById(R.id.phrase_que_close_btn)
        levelNumberTv = view.findViewById(R.id.phrase_que_level_number)
        taskTv = view.findViewById(R.id.phrase_que_task_essence)
        questionTv = view.findViewById(R.id.phrase_que_task_text)
        answerTv = view.findViewById(R.id.phrase_que_answer_first_part)
        missingAnswerPartEt = view.findViewById(R.id.phrase_que_answer)
        continueBtn = view.findViewById(R.id.phrase_que_get_answer_button)
        avatar = view.findViewById(R.id.phrase_que_user_avatar)
        hpBar = view.findViewById(R.id.phrase_que_user_hp_bar)
        expBar = view.findViewById(R.id.phrase_que_user_exp_bar)
        userNameTv = view.findViewById(R.id.phrase_que_username)

        stepBar = view.findViewById(R.id.phrase_que_progress)

        customizeStepViewBar()
        continueBtn.setOnClickListener{listener.onClick()}
    }
}