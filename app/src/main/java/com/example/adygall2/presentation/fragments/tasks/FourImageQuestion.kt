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
import com.example.adygall2.presentation.consts.ArgsKey.TASK_KEY
import com.example.adygall2.presentation.fragments.tasks.base_task.BaseTaskFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Класс, наследующийся от [BaseTaskFragment]
 * Предназначен для взаимодействия с экраном (окном) верификации данных
 * Экран для вопросов с картинками (1 тип заданий)
 */

class FourImageQuestion : BaseTaskFragment(R.layout.fragment_four_image_question) {
    private lateinit var _binding : FragmentFourImageQuestionBinding
    private val binding get() = _binding
    private val viewModel by viewModel<GameViewModel>()

    private var _userAnswer = ""
    override val userAnswer get() = _userAnswer
    private var _rightAnswer = ""
    override val rightAnswer get() = _rightAnswer

    lateinit var imageAdapter : ImageAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFourImageQuestionBinding.inflate(inflater, container, false)

        binding.fourImageTaskEt.text = arguments?.getString(TASK_KEY)

        setObservers()

        return binding.root
    }

    private fun setObservers() {
        viewModel.getAnswers(arguments?.getInt(ID_KEY)!!)
        viewModel.answersListFromDb.observe(viewLifecycleOwner) { dbAnswers ->
            viewModel.getPicturesByAnswers(dbAnswers)
            viewModel.picturesListByAnswersFromDb.observe(viewLifecycleOwner) { dbPics ->
                viewModel.getSoundsByAnswers(dbAnswers)
                viewModel.soundsListByAnswersFromDb.observe(viewLifecycleOwner) { dbSounds ->
                    imageAdapter = ImageAdapter(
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

                    binding.fourImageContainer.adapter = imageAdapter
                    binding.fourImageContainer.layoutManager = GridLayoutManager(requireActivity(), 2)

                    _rightAnswer = dbAnswers.first { it.correctAnswer.toBoolean() }.answer

                }
            }

        }

    }

    /**
     * Чистим кэш Glide для корректной работы адаптера
     */
    override fun onPause() {
        super.onPause()
        imageAdapter.onDestroy {
            viewModel.clearGlideCache()
        }
    }
}