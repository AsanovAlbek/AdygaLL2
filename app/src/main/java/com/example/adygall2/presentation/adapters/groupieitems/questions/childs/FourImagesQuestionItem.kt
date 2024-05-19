package com.example.adygall2.presentation.adapters.groupieitems.questions.childs

import android.content.Context
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.adygall2.R
import com.example.adygall2.data.models.SoundsPlayer
import com.example.adygall2.databinding.FragmentFourImageQuestionBinding
import com.example.adygall2.domain.model.ComplexAnswer
import com.example.adygall2.presentation.adapters.ImageAdapter
import com.example.adygall2.presentation.adapters.groupieitems.questions.parentitem.QuestionItem

class FourImagesQuestionItem(
    private val context: Context,
    private val title: String,
    private val answers: List<ComplexAnswer>,
    private val soundsPlayer: SoundsPlayer,
    private val onClearImages: (() -> Unit)? = null
): QuestionItem<FragmentFourImageQuestionBinding>() {

    private var imageAdapter: ImageAdapter? = null
    private var _userAnswer = ""
    override val userAnswer get() = _userAnswer.replace("[1iLlI|]".toRegex(), "I")

    override fun bind(viewBinding: FragmentFourImageQuestionBinding, position: Int) {
        viewBinding.apply {
            imageAdapter = ImageAdapter(
                context = context,
                complexAnswerList = answers,
                soundsPlayer = soundsPlayer,
                listener = {
                    _userAnswer = it.answer.answer
                }
            )
            fourImageContainer.adapter = imageAdapter
            fourImageContainer.layoutManager = GridLayoutManager(viewBinding.root.context, 2)
            fourImageTaskEt.text = title
        }
    }

    override fun getLayout(): Int = R.layout.fragment_four_image_question

    override fun initializeViewBinding(view: View): FragmentFourImageQuestionBinding =
        FragmentFourImageQuestionBinding.bind(view)

    override val rightAnswer: String =
        answers.first { it.answer.correctAnswer.lowercase().toBoolean() }.answer.answer.replace("[1iLlI|]".toRegex(), "I")

    override fun clear() {
        _userAnswer = ""
        onClearImages?.let { imageAdapter?.onClearImages(it) }
    }
}