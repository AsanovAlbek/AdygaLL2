package com.example.adygall2.presentation.fragments.menu

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.adygall2.R
import com.example.adygall2.databinding.FragmentHomePageBinding
import com.example.adygall2.presentation.view_model.GameViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Класс, имплементирующий интерфейс фрагмента
 * Предназначен для взаимодействия с экраном (окном) выбора селектора
 */

class HomePage : Fragment(R.layout.fragment_home_page) {

    private lateinit var _homePageBinding : FragmentHomePageBinding
    private val homePageBinding get() = _homePageBinding
    private val viewModel by viewModel<GameViewModel>()

    // метод жизненного цикла, вызывается при создании фрагмента (при открытии окна)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _homePageBinding = FragmentHomePageBinding.inflate(inflater, container, false)

        getUserStates()
        homePageBinding.testFlag.flag.setOnClickListener {
            if (homePageBinding.homeBottomBar.userHealthBar.progress > 0) {
                startLesson()
            }
            else {
                dialog("У вас не хватает здоровья, зайдите позже")
            }
        }

        return homePageBinding.root
    }
    /** Получение шкалы здоровья и опыта */
    private fun getUserStates() {

        homePageBinding.homeBottomBar.userHealthBar.progress = arguments!!.getInt("hp")
        homePageBinding.homeBottomBar.userExperienceBar.progress = arguments!!.getInt("exp")
    }

    private fun setListeners() {

    }

    /*private fun observers() {
        viewModel.getTasksFromOrder()
        viewModel.tasksListFromDb.observe(viewLifecycleOwner, ::setAdapter)
    }*/

    /*private fun setAdapter(tasks : List<Task>) {
        binding.levelsPager.adapter = LevelsAdapter(tasks) {
            val bundle = Bundle()
            if (binding.homeBottomBar.userHealthBar.progress > 0) {
                bundle.putParcelableArray("taskList", it.toTypedArray())
                findNavController().navigate(R.id.action_homePage_to_taskContainer, bundle)
            }
            else {
                dialog("У вас не осталось здоровья, зайдите позже")
            }

        }
    }*/

    private fun dialog(message : String) {
        AlertDialog.Builder(requireActivity())
            .setMessage(message).create().show()
    }

    /**
     * Метод для входа в урок, передаётся информация о хп и опыте
     */
    private fun startLesson() {
        val userHpAndExp = Bundle()
        userHpAndExp.putInt("hp", homePageBinding.homeBottomBar.userHealthBar.progress)
        userHpAndExp.putInt("exp", homePageBinding.homeBottomBar.userExperienceBar.progress)
        findNavController().navigate(R.id.action_homePage_to_taskContainer, userHpAndExp)
    }
}