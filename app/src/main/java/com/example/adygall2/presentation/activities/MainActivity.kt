package com.example.adygall2.presentation.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.adygall2.R
import com.example.adygall2.databinding.ActivityMainBinding
import com.example.adygall2.presentation.fragments.*

/**
 * Класс - Activity для создания и взаимодействия с окнами
 */

class MainActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityMainBinding
    private val binding get() = _binding
    //private val navController by lazy { Navigation.findNavController(this, R.id.navHost) }

    // Метод, вызываемый при создании activity (в нашем случае, при открытии приложения)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        _binding = ActivityMainBinding.inflate(layoutInflater)

        val navHost = supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        val navController = navHost.navController
    }
}