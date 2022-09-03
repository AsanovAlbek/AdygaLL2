package com.example.adygall2.presentation.fragments.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.adygall2.R
import com.example.adygall2.databinding.FragmentLessonResultsBinding

/**
 * Фрагмент для показа результатов урока
 * Пока что не проработано
 */
class TaskResults : Fragment(R.layout.fragment_lesson_results) {

    private lateinit var _lessonResultBinding : FragmentLessonResultsBinding
    private val lessonResultsBinding get() = _lessonResultBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _lessonResultBinding = FragmentLessonResultsBinding.inflate(inflater, container, false)

        // Слушатель на нажатие кнопки
        lessonResultsBinding.completeLessonBtn.setOnClickListener {
            exitIntoLevel()
        }

        return lessonResultsBinding.root
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