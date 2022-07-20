package com.example.adygall2.presentation.fragments.tasks

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.adygall2.R
import com.example.adygall2.data.db_models.Picture
import com.example.adygall2.databinding.FragmentFourImageQuestionBinding
import com.example.adygall2.presentation.GameViewModel
import com.example.adygall2.presentation.adapters.ImageAdapter
import com.example.adygall2.presentation.consts.ArgsKey.ID_KEY
import com.example.adygall2.presentation.consts.ArgsKey.MY_LOG_TAG
import com.example.adygall2.presentation.consts.ArgsKey.TASK_KEY
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Класс, имплементирующий интерфейс фрагмента
 * Предназначен для взаимодействия с экраном (окном) верификации данных
 * Экран для вопросов с картинками
 */

class FourImageQuestion : Fragment(R.layout.fragment_four_image_question) {
    private lateinit var _binding : FragmentFourImageQuestionBinding
    private val binding get() = _binding
    private val viewModel by viewModel<GameViewModel>()

    private var _userAnswer = ""
    val userAnswer get() = _userAnswer
    private var _rightAnswer = ""
    val rightAnswer get() = _rightAnswer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(MY_LOG_TAG, "Задание с картинками создано")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFourImageQuestionBinding.inflate(inflater, container, false)

        binding.fourImageTaskEt.text = arguments?.getString(TASK_KEY)

        viewModel.getAnswers(arguments?.getInt(ID_KEY)!!)

        setObservers()
        saveBundle()

        return binding.root
    }

    private fun setObservers() {
        viewModel.answersListFromDb.observe(viewLifecycleOwner) {dbAnswers ->
            viewModel.getPicturesByAnswers(dbAnswers)

            viewModel.picturesListByAnswersFromDb.observe(viewLifecycleOwner) { dbPics ->
                val adapter = ImageAdapter(dbPics, object : ImageAdapter.OnSelectClickListener {
                    override fun onSelect(position: Int, picture: Picture) {
                        _userAnswer = picture.name
                    }
                })
                binding.fourImageContainer.adapter = adapter
                binding.fourImageContainer.layoutManager = GridLayoutManager(requireActivity(), 2)

                // Получение правильного ответа
                _rightAnswer = dbAnswers.firstOrNull {
                        answer -> answer.correctAnswer.toBoolean() }?.answer ?: "Правильный ответ не найден"

                Log.i(MY_LOG_TAG, "Вопрос с картинками: userAnswer = $userAnswer" +
                        "rightAnswer = $rightAnswer, currentItem = " +
                        adapter.getSelectedItem().toString()
                )
            }
        }

        val bundle = Bundle()
        bundle.apply {
            putString("rightAnswer", rightAnswer)
            putString("userAnswer", userAnswer)
        }
        this.arguments = bundle
    }

    private fun saveBundle() {

    }

}