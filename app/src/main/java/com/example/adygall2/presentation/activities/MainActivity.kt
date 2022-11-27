package com.example.adygall2.presentation.activities

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.edit
import androidx.navigation.fragment.NavHostFragment
import com.example.adygall2.R
import com.example.adygall2.data.local.PrefConst
import com.example.adygall2.databinding.ActivityMainBinding
import com.example.adygall2.presentation.view_model.GameViewModel
import java.util.Date
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named

/**
 * Класс - Activity для создания и взаимодействия с окнами
 */

class MainActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityMainBinding
    private val binding get() = _binding
    private val viewModel by viewModels<GameViewModel>()

    private val timePref: SharedPreferences by inject(named(PrefConst.DATE))
    private val userHpPref: SharedPreferences by inject(named(PrefConst.USER_HP))
    private val expPref: SharedPreferences by inject(named(PrefConst.USER_EXP))

    companion object {
        // Время в минутах, за которое увеличивается здоровье на 10
        const val HEALTH_REGENERATE_VALUE = 10
        const val HEALTH_REGENERATE_TIME = 5
        const val MILLIS_IN_SECOND = 1000
        const val SECOND_IN_MINUTE = 60
        const val DEFAULT_TIME = 0L
        const val DEFAULT_HP = 100
        const val DEFAULT_EXP = 0
        const val MAX_HP = 100
    }

    // При открытии приложения вычисляется, сколько восстановить здоровья игроку
    override fun onStart() {
        super.onStart()
        regenerateHealthOffline()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        _binding = ActivityMainBinding.inflate(layoutInflater)

        val navHost = supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        val navController = navHost.navController

    }

    // При закрытии приложения записывается время выхода
    override fun onStop() {
        super.onStop()
        val currentDate = Date()
        timePref.edit { putLong(PrefConst.DATE, currentDate.time) }
        //viewModel.clearGlideCache()
    }

    private fun regenerateHealthOffline() {
        // Время в данный момент
        val now = Date()
        // Время последнего выхода
        val lastExitTimeMillis = timePref.getLong(PrefConst.DATE, DEFAULT_TIME)
        // Значение здоровья пользователя
        var userHealth = userHpPref.getInt(PrefConst.USER_HP, MAX_HP)
        // Разница во времени между выходом и входом
        val period = now.time - lastExitTimeMillis
        // В минутах
        val minutes = period / MILLIS_IN_SECOND / SECOND_IN_MINUTE
        // Высчитываем, сколько раз прошло по 5 минут и восстанавливаем здоровье на 10 за каждые 5 минут
        userHealth += ((minutes / HEALTH_REGENERATE_TIME).toInt() * HEALTH_REGENERATE_VALUE)
        // Если получилось значение больше 100, то делаем значение 100
        if (userHealth > MAX_HP) userHealth = MAX_HP
        // Записываем здоровье в SharedPreference
        userHpPref.edit { putInt(PrefConst.USER_HP, userHealth) }
    }
}