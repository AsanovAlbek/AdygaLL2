package com.example.adygall2.presentation.fragments.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.adygall2.R
import com.example.adygall2.databinding.FragmentLessonResultsBinding

class FragmentGameResults : Fragment(R.layout.fragment_lesson_results) {

    private lateinit var _lessonResultBinding : FragmentLessonResultsBinding
    private val lessonResultsBinding get() = _lessonResultBinding
    private val resultsArgs: FragmentGameResultsArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _lessonResultBinding = FragmentLessonResultsBinding.inflate(inflater, container, false)
        return lessonResultsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Слушатель на нажатие кнопки
        lessonResultsBinding.completeLessonBtn.setOnClickListener {
            exitIntoLevel()
        }
        setStatisticByArguments()
    }

    /** Получение статистики из аргументов */
    private fun setStatisticByArguments() {
        lessonResultsBinding.apply {
            newLearnedWordsCount.text = resultsArgs.learnedWords.toString()
            mistakesCount.text = resultsArgs.mistakes.toString()
            livesCount.text = resultsArgs.lives.toString()
            coinsCount.text = resultsArgs.coins.toString()
            passedTime.text = resultsArgs.time
        }
    }

    /**
     * Метод, вызываемый при выходе из окна с результатами
     * Получает значения "здоровье" и "опыт" из окна с заданиями
     * И передаёт их дальше в домашнюю страницу
     */
    private fun exitIntoLevel() {
        val action = FragmentGameResultsDirections.actionTaskResultsToHomePage(
            hp = resultsArgs.hp,
            exp = resultsArgs.exp
        )
        findNavController().navigate(action)
    }
}