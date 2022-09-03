package com.example.adygall2.presentation.fragments.tasks

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import com.example.adygall2.R
import com.example.adygall2.data.db_models.Answer
import com.example.adygall2.databinding.FragmentTranslateTheTextBinding
import com.example.adygall2.presentation.view_model.GameViewModel
import com.example.adygall2.presentation.adapters.SentenceAdapter
import com.example.adygall2.presentation.adapters.adapter_handles.AdapterCallback
import com.example.adygall2.presentation.adapters.adapter_handles.HandleDragAndDropEvent
import com.example.adygall2.presentation.consts.ArgsKey.ID_KEY
import com.example.adygall2.presentation.consts.ArgsKey.TASK_KEY
import com.example.adygall2.presentation.fragments.tasks.base_task.BaseTaskFragment
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.tomergoldst.tooltips.ToolTip
import com.tomergoldst.tooltips.ToolTipsManager
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Фрагмент для задания с переводом текста построением предложения из кнопок со словами
 */
class TranslateTheTextTask : BaseTaskFragment(R.layout.fragment_translate_the_text), AdapterCallback {

    private lateinit var _binding: FragmentTranslateTheTextBinding
    private val binding get() = _binding
    private val viewModel by viewModel<GameViewModel>()
    private var _userAnswer = String()
    override val userAnswer get() = _userAnswer
    private var _rightAnswer = String()
    override val rightAnswer get() = _rightAnswer

    private lateinit var userAdapter : SentenceAdapter
    private lateinit var answerAdapter : SentenceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTranslateTheTextBinding.inflate(inflater, container, false)
        setTaskText()
        setObservers()

        return binding.root
    }

    private fun setObservers() {
        val taskId = arguments?.getInt(ID_KEY)
        viewModel.getAnswers(taskId!!)
        viewModel.answersListFromDb.observe(viewLifecycleOwner, ::setAdapters)
    }

    private fun setAdapters(answers: MutableList<Answer>) {
        val mutableAnswers = answers.map { it.answer }.toMutableList()
        userAdapter = SentenceAdapter(requireContext(), true, mutableAnswers, this)
        answerAdapter = SentenceAdapter(requireContext(), false, mutableListOf(), this)

        binding.answersRecycler.adapter = answerAdapter
        binding.userBarRecycler.adapter = userAdapter

        binding.answersRecycler.setOnDragListener { _, dragEvent ->
            HandleDragAndDropEvent(dragEvent).handle(this, false, -1)
            true
        }
        binding.userBarRecycler.setOnDragListener { _, dragEvent ->
            HandleDragAndDropEvent(dragEvent).handle(this, true, -1)
            true
        }

        (binding.answersRecycler.layoutManager as FlexboxLayoutManager).flexDirection =
            FlexDirection.ROW
        (binding.userBarRecycler.layoutManager as FlexboxLayoutManager).flexDirection =
            FlexDirection.ROW

        val rightAnswerStroke = answers.first().correctAnswer
        _rightAnswer = viewModel.transform(rightAnswerStroke)
    }

    /**
     * Метод для подготовки всех текстов для задания
     */
    private fun setTaskText() {
        val tooltipsManagersList = mutableListOf<ToolTipsManager>()
        // Получаем текст задания
        val taskText = arguments?.getString(TASK_KEY)
        // Разделяем на две части, одна на русском, другая на адыгейском
        val taskParts = taskText?.split("*")
        // Вторую часть преобразуем в лист строк, разделённых #
        val hintsForTaskText = taskParts?.get(1)?.split("#")
        taskParts?.get(0)?.split("#")?.forEachIndexed { i, it ->
            // Добавляемое подчёркнутое слово
            val addedWord = SpannableString(it)
            // Установка подчёркивания
            addedWord.setSpan(UnderlineSpan(), 0, addedWord.length, 0)
            val addedTextView = TextView(requireActivity())
            setTextViewParams(addedTextView, addedWord)
            // Получение билдера для подсказок
            val tooltipManager = ToolTipsManager()
            tooltipManager.findAndDismiss(addedTextView)
            val tooltipBuilder = setTooltip(addedTextView, hintsForTaskText?.get(i).toString())
            tooltipsManagersList.add(tooltipManager)
            // Если есть подсказки, то присваиваем соответсвующую подсказку к TextView
            addedTextView.setOnClickListener {
                tooltipsManagersList.forEach { it.dismissAll() }
                tooltipManager.show(tooltipBuilder.build())
            }
            binding.wordsWithTooltip.addView(addedTextView)
        }
    }

    /**
     * Метод для установки визуальных значений
     * @param textView - TextView, для которого они устанавливаются
     * @param textInView - текст, устанавливаемый в TextView
     */
    private fun setTextViewParams(textView: TextView, textInView: SpannableString) =
        textView.apply {
            text = textInView
            setBackgroundColor(requireActivity().resources.getColor(R.color.gray_91, null))
            setPadding(10)
            textSize = 18F
            setTextColor(Color.BLACK)
        }

    /**
     * Метод для установки подсказки
     * @param textView - TextView, для которого будет эта подсказка
     * @param text - текст подсказки
     */
    private fun setTooltip(textView: TextView, text : String) : ToolTip.Builder {
        val toolTipBuilder = ToolTip.Builder(
            requireContext(), textView, binding.rootForTooltip,
            text, ToolTip.POSITION_ABOVE
        )
        toolTipBuilder.apply {
            setAlign(ToolTip.ALIGN_CENTER)
            setBackgroundColor(resources.getColor(R.color.persian_orange, null))
        }

        return toolTipBuilder
    }

    /** Метод для изменения содержимого адаптеров */
    override fun change(isFirstAdapter: Boolean, item: String, position: Int) {
        if (isFirstAdapter) {
            userAdapter.addAnswer(item, position)
            answerAdapter.removeAnswer(item)
        }
        else {
            userAdapter.removeAnswer(item)
            answerAdapter.addAnswer(item, position)
        }
        _userAnswer = viewModel.transform(answerAdapter.adapterItems.joinToString())
    }
}