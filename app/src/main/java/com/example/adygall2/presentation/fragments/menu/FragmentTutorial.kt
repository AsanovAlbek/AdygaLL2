package com.example.adygall2.presentation.fragments.menu

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.adygall2.R
import com.example.adygall2.databinding.FragmentAppTutorialBinding

class FragmentTutorial: Fragment(R.layout.fragment_app_tutorial) {

    private var _binding: FragmentAppTutorialBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppTutorialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.tutorialText.movementMethod = ScrollingMovementMethod()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}