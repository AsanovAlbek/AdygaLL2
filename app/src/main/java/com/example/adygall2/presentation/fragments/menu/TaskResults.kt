package com.example.adygall2.presentation.fragments.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.adygall2.R
import com.example.adygall2.databinding.FragmentLessonResultsBinding

class TaskResults : Fragment(R.layout.fragment_lesson_results) {

    private lateinit var _binding : FragmentLessonResultsBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLessonResultsBinding.inflate(inflater, container, false)

        // Слушатель на нажатие кнопки
        binding.completeLessonBtn.setOnClickListener {
            exitIntoLevel()
        }

        return binding.root
    }

    /**
     * Метод, вызываемый при выходе из окна с результатами
     * Получает значения "здоровье" и "опыт" из окна с заданиями
     * И передаёт их дальше в домашнюю страницу
     */
    private fun exitIntoLevel() {
        val bundle = Bundle()
        val hpProgress = arguments?.getInt("hp")
        val expProgress = arguments?.getInt("exp")
        bundle.putInt("hp", hpProgress!!)
        bundle.putInt("exp", expProgress!!)
        findNavController().navigate(R.id.action_taskResults_to_homePage, bundle)
    }
}