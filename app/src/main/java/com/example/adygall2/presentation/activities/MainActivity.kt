package com.example.adygall2.presentation.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.fragment.NavHostFragment
import com.example.adygall2.R
import com.example.adygall2.databinding.ActivityMainBinding
import com.example.adygall2.presentation.const.LastNavigationPage.GAME_SCREEN
import com.example.adygall2.presentation.const.LastNavigationPage.HOME_SCREEN
import com.example.adygall2.presentation.const.LastNavigationPage.RESULT_SCREEN
import com.example.adygall2.presentation.const.LastNavigationPage.SIGN_UP_SCREEN
import com.example.adygall2.presentation.view_model.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Класс - Activity для создания и взаимодействия с окнами
 */

class MainActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityMainBinding
    private val binding get() = _binding
    private val viewModel by viewModel<MainViewModel>()
    private lateinit var navHost: NavHostFragment
    private lateinit var navController: NavController

    // При открытии приложения вычисляется, сколько восстановить здоровья игроку
    override fun onStart() {
        super.onStart()
        viewModel.regenerateHealthOffline()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        navHost = supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        navController = navHost.navController
        setupStartDestination()
    }

    // При закрытии приложения записывается время выхода
    override fun onStop() {
        super.onStop()
        viewModel.saveExitTime()
    }

    override fun onBackPressed() {
        viewModel.backPressedAlertDialog(this)
    }

    private fun setupStartDestination() {

        // Получаем граф навигации
        val navGraph = navController.navInflater.inflate(R.navigation.game_nav)
        // Получаем данные о пользователе, авторизировался ли он
        val isUserSignUp = viewModel.user.isUserSignUp
        if (isUserSignUp) {
            // Если да, то начинаем сразу с главного экрана
            navGraph.setStartDestination(R.id.homePage)
        } else {
            // Иначе начинаем с экрана авторизации
            navGraph.setStartDestination(R.id.authorize)
        }

        navController.graph = navGraph
    }
}