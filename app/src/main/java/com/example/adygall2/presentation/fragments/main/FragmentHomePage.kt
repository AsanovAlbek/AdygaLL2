package com.example.adygall2.presentation.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adygall2.R
import com.example.adygall2.databinding.FragmentNewHomePageBinding
import com.example.adygall2.domain.model.Task
import com.example.adygall2.domain.model.User
import com.example.adygall2.presentation.activities.MainActivity
import com.example.adygall2.presentation.activities.UserChangeListener
import com.example.adygall2.presentation.adapters.LevelsAdapter
import com.example.adygall2.presentation.view_model.HomeViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Класс, имплементирующий интерфейс фрагмента
 * Предназначен для взаимодействия с экраном (окном) выбора селектора
 */

class FragmentHomePage : Fragment(R.layout.fragment_new_home_page) {

    private var _homePageBinding: FragmentNewHomePageBinding? = null
    private val homePageBinding get() = _homePageBinding!!
    private val viewModel by viewModel<HomeViewModel>()
    private var userChangeListener: UserChangeListener? = null

    // метод жизненного цикла, вызывается при создании фрагмента (при открытии окна)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _homePageBinding = FragmentNewHomePageBinding.inflate(inflater, container, false)
        userChangeListener = requireActivity() as MainActivity
        observe()

        return homePageBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }

    override fun onStop() {
        super.onStop()
        homePageBinding.homeBottomBar.apply {
            viewModel.saveUserStates(hp = hp.progress, coins = exp.progress)
        }
    }

    override fun onDestroy() {
        userChangeListener?.getUserHealthLiveData()?.removeObservers(viewLifecycleOwner)
        _homePageBinding = null
        userChangeListener = null
        super.onDestroy()
    }

    private fun observe() {
        viewModel.tasksListFromDb.observe(viewLifecycleOwner, ::levelsTree)
        viewModel.observableUser.observe(viewLifecycleOwner, ::observeUser)
        viewModel.getTasksFromOrder()
        userChangeListener?.getUserHealthLiveData()?.observe(viewLifecycleOwner, ::observeHp)
    }

    private fun observeHp(health: Int) {
        homePageBinding.homeBottomBar.hp.progress = health
    }

    private fun observeUser(user: User) {
        homePageBinding.homeBottomBar.apply {
            userNameTv.text = user.name
            exp.progress = user.coins
            userAvatar.setImageBitmap(viewModel.getPhotoFromCache())
        }
    }

    private fun levelsTree(list: List<Task>) {
        val adapter = LevelsAdapter(
            tasks = list,
            lessonClickEvent = { levelNumber, number, adapterList, levelName ->
                openTasks(levelNumber, number, adapterList, levelName)
            },
            userProgress = viewModel.user.learningProgressSet
        )
        homePageBinding.levelItems.adapter = adapter
        homePageBinding.levelItems.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun openTasks(
        levelNumber: Int,
        lessonNumber: Int,
        tasks: List<Task>,
        levelName: String
    ) {
//        if (homePageBinding.homeBottomBar.hp.progress > 0) {
//            viewModel.openLesson(
//                level = levelNumber,
//                lesson = lessonNumber,
//                tasks = tasks,
//                navController = findNavController(),
//                levelName = levelName
//            )
//        } else {
//            viewModel.noHpMessage(requireContext())
//        }
        viewModel.openLesson(
                level = levelNumber,
                lesson = lessonNumber,
                tasks = tasks,
                navController = findNavController(),
                levelName = levelName
            )
    }
}