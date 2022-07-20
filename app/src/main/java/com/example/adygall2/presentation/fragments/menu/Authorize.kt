package com.example.adygall2.presentation.fragments.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.adygall2.R
import com.example.adygall2.databinding.FragmentAuthorizeBinding

/**
 * Класс, имплементирующий интерфейс фрагмента
 * Предназначен для взаимодействия с экраном (окном) ввода имени пользователя и пароля
 */

class Authorize : Fragment(R.layout.fragment_authorize) {

    private lateinit var _binding: FragmentAuthorizeBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAuthorizeBinding.inflate(inflater, container, false)

        binding.authSingInButton.setOnClickListener {
                findNavController().navigate(R.id.action_authorize_to_homePage)
        }
        return binding.root
    }
}