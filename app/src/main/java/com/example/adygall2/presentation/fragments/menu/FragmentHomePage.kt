package com.example.adygall2.presentation.fragments.menu

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adygall2.R
import com.example.adygall2.data.local.PrefConst
import com.example.adygall2.databinding.FragmentNewHomePageBinding
import com.example.adygall2.domain.model.Task
import com.example.adygall2.presentation.adapters.LevelsAdapter
import com.example.adygall2.presentation.fragments.dialog
import com.example.adygall2.presentation.view_model.GameViewModel
import java.util.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named

/**
 * Класс, имплементирующий интерфейс фрагмента
 * Предназначен для взаимодействия с экраном (окном) выбора селектора
 */

class FragmentHomePage : Fragment(R.layout.fragment_new_home_page) {

    private lateinit var _homePageBinding: FragmentNewHomePageBinding
    private val homePageBinding get() = _homePageBinding
    private val viewModel by viewModel<GameViewModel>()

    private val userHpPrefs : SharedPreferences by inject(named(PrefConst.USER_HP))
    private val userExpPrefs : SharedPreferences by inject(named(PrefConst.USER_EXP))
    private val levelProgressPrefs : SharedPreferences by inject(named(PrefConst.LEVEL_PROGRESS))
    private val lessonSharedPreferences: SharedPreferences by inject(named(PrefConst.LESSON_PROGRESS))

    companion object {
        const val DEFAULT_HP = 100
        const val DEFAULT_EXP = 0
        const val DEFAULT_LESSON_PROGRESS = 0
        const val DEFAULT_LEVEL_PROGRESS = 1
    }

    // метод жизненного цикла, вызывается при создании фрагмента (при открытии окна)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _homePageBinding = FragmentNewHomePageBinding.inflate(inflater, container, false)

        getUserStates()
        observe()

        return homePageBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        hillHp()
    }

    override fun onStop() {
        super.onStop()
        userHpPrefs.edit {
            // Сохраняем здоровье и монеты при выходе
            putInt(PrefConst.USER_HP,homePageBinding.homeBottomBar.hp.progress)
        }
        userExpPrefs.edit {
            putInt(PrefConst.USER_EXP, homePageBinding.homeBottomBar.exp.progress)
        }
    }

    /** Получение шкалы здоровья и опыта */
    private fun getUserStates() {
        homePageBinding.homeBottomBar.apply {
            hp.progress = userHpPrefs.getInt(PrefConst.USER_HP, DEFAULT_HP)
            exp.progress = userExpPrefs.getInt(PrefConst.USER_EXP, DEFAULT_EXP)
        }
    }

    private fun observe() {
        viewModel.tasksListFromDb.observe(viewLifecycleOwner, ::levelsTree)
        viewModel.getTasksFromOrder()
    }

    private fun levelsTree(list: List<Task>) {
        val adapter = LevelsAdapter(
            context = requireContext(),
            tasks = list,
            completedLevel = levelProgressPrefs.getInt(PrefConst.LEVEL_PROGRESS, DEFAULT_LEVEL_PROGRESS),
            lessonClickEvent = {
                    levelNumber: Int, number: Int, adapterList: List<Task> -> openTasks(levelNumber, number, adapterList)
            },
            completedLesson = lessonSharedPreferences.getInt(PrefConst.LESSON_PROGRESS, DEFAULT_LESSON_PROGRESS)
        )
        homePageBinding.levelItems.adapter = adapter
        homePageBinding.levelItems.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun openTasks(levelNumber: Int, lessonNumber: Int, tasks: List<Task>) {
        if (homePageBinding.homeBottomBar.hp.progress > 0) {
            val action = FragmentHomePageDirections.actionHomePageToTaskContainer(
                tasks = tasks.toTypedArray(),
                hp = homePageBinding.homeBottomBar.hp.progress,
                exp = homePageBinding.homeBottomBar.exp.progress,
                lessonProgress = lessonNumber,
                levelProgress = levelNumber
            )
            findNavController().navigate(action)
        } else {
            dialog("У вас не достаточно здоровья, зайдите позже")
        }
    }

    private fun hillHp() {

    }
}