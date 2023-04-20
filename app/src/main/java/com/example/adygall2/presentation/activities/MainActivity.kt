package com.example.adygall2.presentation.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.adygall2.R
import com.example.adygall2.databinding.ActivityMainBinding
import com.example.adygall2.presentation.model.UserProfileState
import com.example.adygall2.presentation.view_model.MainViewModel
import de.hdodenhof.circleimageview.CircleImageView
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
    private lateinit var appBarConfiguration: AppBarConfiguration
    private var drawerHeader: View? = null

    // При открытии приложения вычисляется, сколько восстановить здоровья игроку
    override fun onStart() {
        super.onStart()
        viewModel.regenerateHealthOffline()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHost = supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        navController = navHost.navController
        setupDrawer()
        setupStartDestination()
        setupToolbarVisible()
        observe()
        viewModel.userStates()
    }

    // Метод для того, чтобы вызвать его после изменения данных пользователя и обновить данные хидера
    // Вызывается из фрагментов FragmentAuth и FragmentEditUser
    fun updateUserStates() {
        viewModel.userStates()
    }

    // Установка видимости тулбара
    private fun setupToolbarVisible() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id) {
                R.id.userProfile,
                R.id.editUserProfile,
                R.id.aboutApp,
                R.id.homePage,
                R.id.appTutorial,
                R.id.contactUs-> showActionBar()
                else -> hideActionBar()
            }
        }
    }

    private fun hideActionBar() {
        supportActionBar?.hide()
        // Прячет Action Bar
        window.insetsController?.hide(WindowInsetsCompat.Type.systemBars())
    }

    private fun showActionBar() {
        // Показывает Action Bar
        supportActionBar?.show()
        window.insetsController?.show(WindowInsetsCompat.Type.systemBars())
    }

    private fun observe() {
        viewModel.state.observe(this, ::observeUser)
    }

    private fun setupDrawer() {
        setSupportActionBar(binding.mainBar.mainToolbar)
        // Убираем заголовок action bar
        supportActionBar?.setDisplayShowTitleEnabled(false)
        // Здесь фрагменты, которые будут в Navigation Drawer
        // id обязательно должны быть одинаковыми с id элементов из меню
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homePage), binding.drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
        // Получаем header
        drawerHeader = binding.navView.getHeaderView(0)
        // Вешаем на него обработчик нажатий
        drawerHeader?.setOnClickListener {
            navController.navigate(R.id.userProfile)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    private fun observeUser(userProfileState: UserProfileState) {
        // Получаем вьюшки хидера
        val userNameTextView = drawerHeader?.findViewById<TextView>(R.id.userName)
        val userImage = drawerHeader?.findViewById<CircleImageView>(R.id.userImage)
        // Меняем их в соответсвии с аватаром и именем пользователя
        userNameTextView?.text = userProfileState.name
        userImage?.setImageBitmap(userProfileState.photo)
    }

    // При закрытии приложения записывается время выхода
    override fun onStop() {
        super.onStop()
        viewModel.saveExitTime()
    }

    // Переопределили, чтобы работал бургер
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    // Кастомизация нажатия кнопки назад
    override fun onBackPressed() {
        when(navController.currentDestination?.id) {
            // Диалоговое окно с выходом из приложения
            R.id.homePage, R.id.authorize -> viewModel.exitFromAppDialog(this)
            // Диалоговое окно с выходом из урока
            R.id.taskContainer -> viewModel.exitFromLessonDialog(this, navController)
            // Просто возврат на главную страницу
            R.id.taskResults,
            R.id.userProfile,
            R.id.appTutorial,
            R.id.aboutApp,
            R.id.contactUs-> navController.navigate(R.id.homePage)
            // Иначе ничего не меняем
            else -> super.onBackPressed()
        }
    }

    // В зависимости от того, зарегистрирован ли пользователь, показать ему нужный экран
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

    override fun onDestroy() {
        // Во избежание утечек в памяти, обнуляем хидер бокового меню
        drawerHeader = null
        super.onDestroy()
    }
}