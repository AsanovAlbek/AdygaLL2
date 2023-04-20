package com.example.adygall2.presentation.fragments.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adygall2.R
import com.example.adygall2.databinding.FragmentNewHomePageBinding
import com.example.adygall2.domain.model.Task
import com.example.adygall2.presentation.adapters.LevelsAdapter
import com.example.adygall2.presentation.view_model.HomeViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Класс, имплементирующий интерфейс фрагмента
 * Предназначен для взаимодействия с экраном (окном) выбора селектора
 */

class FragmentHomePage : Fragment(R.layout.fragment_new_home_page) {

    private lateinit var _homePageBinding: FragmentNewHomePageBinding
    private val homePageBinding get() = _homePageBinding
    private val viewModel by viewModel<HomeViewModel>()

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
        viewModel.initAutoHill(homePageBinding.homeBottomBar.hp.progress)
        hillHp()
    }

    override fun onStop() {
        super.onStop()
        homePageBinding.homeBottomBar.apply {
            viewModel.saveUserStates(hp = hp.progress, coins = exp.progress)
        }
    }

    /** Получение шкалы здоровья и опыта */
    private fun getUserStates() {
        homePageBinding.homeBottomBar.apply {
            hp.progress = viewModel.user.hp
            exp.progress = viewModel.user.coins
            userNameTv.text = viewModel.user.name
            userAvatar.setImageBitmap(viewModel.getPhotoFromCache())
        }
    }

    private fun observe() {
        viewModel.tasksListFromDb.observe(viewLifecycleOwner, ::levelsTree)
        viewModel.getTasksFromOrder()
    }

    private fun levelsTree(list: List<Task>) {
        val adapter = LevelsAdapter(
            tasks = list,
            lessonClickEvent = { levelNumber, number, adapterList -> openTasks(levelNumber, number, adapterList) },
            userProgress = viewModel.user.learningProgressSet
        )
        homePageBinding.levelItems.adapter = adapter
        homePageBinding.levelItems.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun openTasks(levelNumber: Int, lessonNumber: Int, tasks: List<Task>) {
        if (homePageBinding.homeBottomBar.hp.progress > 0) {
            viewModel.openLesson(
                level = levelNumber,
                lesson = lessonNumber,
                tasks = tasks,
                navController = findNavController()
            )
            Log.i("home", "level = $levelNumber lesson = $lessonNumber")
        } else {
            viewModel.noHpMessage(requireContext())
        }
    }

    private fun hillHp() {
        if (homePageBinding.homeBottomBar.hp.progress < 100) {
            viewModel.viewModelScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.autoHillHp()
                    viewModel.hpHill.collect {
                        homePageBinding.homeBottomBar.hp.progress = it
                    }
                }
            }
        }
    }
}