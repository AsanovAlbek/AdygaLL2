package com.example.adygall2.presentation.fragments.menu

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.adygall2.R
import com.example.adygall2.databinding.FragmentInfoAboutCompanyBinding

class FragmentAboutApp: Fragment(R.layout.fragment_info_about_company) {
    private var _binding: FragmentInfoAboutCompanyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoAboutCompanyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.aboutAppText.movementMethod = ScrollingMovementMethod()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}