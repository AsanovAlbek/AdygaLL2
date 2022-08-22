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
import com.example.adygall2.databinding.FragmentFourImageQuestionBinding
import com.example.adygall2.presentation.view_model.GameViewModel
import com.example.adygall2.presentation.adapters.ImageAdapter
import com.example.adygall2.presentation.consts.ArgsKey.ID_KEY
import com.example.adygall2.presentation.consts.ArgsKey.MY_LOG_TAG
import com.example.adygall2.presentation.consts.ArgsKey.TASK_KEY
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Класс, имплементирующий интерфейс фрагмента
 * Предназначен для взаимодействия с экраном (окном) верификации данных
 * Экран для вопросов с картинками (1 тип заданий)
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

        setObservers()
        saveBundle()

        return binding.root
    }

    private fun setObservers() {
        viewModel.getAnswers(arguments?.getInt(ID_KEY)!!)
        viewModel.answersListFromDb.observe(viewLifecycleOwner) { dbAnswers ->
            viewModel.getPicturesByAnswers(dbAnswers)
            viewModel.picturesListByAnswersFromDb.observe(viewLifecycleOwner) { dbPics ->
                viewModel.getSoundsByAnswers(dbAnswers)
                viewModel.soundsListByAnswersFromDb.observe(viewLifecycleOwner) { dbSounds ->
                    val adapter = ImageAdapter(
                        context = requireActivity(),
                        answers = dbAnswers,
                        pictures = dbPics,
                        sounds = dbSounds,
                        listener = object : ImageAdapter.OnSelectClickListener {
                            override fun onSelect(position: Int, answer: Answer) {
                                _userAnswer = answer.answer
                            }
                        }
                    )

                    binding.fourImageContainer.adapter = adapter
                    binding.fourImageContainer.layoutManager = GridLayoutManager(requireActivity(), 2)

                    _rightAnswer = dbAnswers.first { it.correctAnswer.toBoolean() }.answer

                }
            }

        }

    }

    private fun saveBundle() {

    }

}