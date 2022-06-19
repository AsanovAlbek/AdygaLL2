package com.example.adygall2.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.adygall2.R
import com.example.adygall2.databinding.FragmentPhraseCompleteBinding
import com.example.adygall2.presentation.consts.ArgsKey

class PhraseFragment : Fragment(R.layout.fragment_phrase_complete) {

    private lateinit var _binding : FragmentPhraseCompleteBinding
    private val binding get() = _binding
    //private val viewModel by viewModel<GameViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(ArgsKey.MY_LOG_TAG, "Задание с дополнением предложения создано")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhraseCompleteBinding.inflate(inflater, container, false)

        binding.phraseTaskTv.text = arguments?.getString(ArgsKey.TASK_KEY)

        return binding.root
    }
}