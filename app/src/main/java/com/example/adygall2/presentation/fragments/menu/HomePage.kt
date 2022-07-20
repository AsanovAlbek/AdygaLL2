package com.example.adygall2.presentation.fragments.menu

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.adygall2.R
import com.example.adygall2.data.db_models.Task
import com.example.adygall2.databinding.FragmentHomePageBinding
import com.example.adygall2.presentation.GameViewModel
import com.example.adygall2.presentation.adapters.LevelsAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Класс, имплементирующий интерфейс фрагмента
 * Предназначен для взаимодействия с экраном (окном) выбора селектора
 */

class HomePage : Fragment(R.layout.fragment_home_page) {

    private lateinit var _binding : FragmentHomePageBinding
    private val binding get() = _binding
    private val viewModel by viewModel<GameViewModel>()

    // метод жизненного цикла, вызывается при создании фрагмента (при открытии окна)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentHomePageBinding.inflate(inflater, container, false)

        getUserStates()
        binding.testFlag.flag.setOnClickListener {
            if (binding.homeBottomBar.userHealthBar.progress > 0) {
                startLesson()
            }
            else {
                dialog("У вас не хватает здоровья, зайдите позже")
            }
        }

        return binding.root
    }

    private fun getUserStates() {
        // Получение шкалы здоровья и опыта
        binding.homeBottomBar.userHealthBar.progress = arguments!!.getInt("hp")
        binding.homeBottomBar.userExperienceBar.progress = arguments!!.getInt("exp")
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
        userHpAndExp.putInt("hp", binding.homeBottomBar.userHealthBar.progress)
        userHpAndExp.putInt("exp", binding.homeBottomBar.userExperienceBar.progress)
        findNavController().navigate(R.id.action_homePage_to_taskContainer, userHpAndExp)
    }
}