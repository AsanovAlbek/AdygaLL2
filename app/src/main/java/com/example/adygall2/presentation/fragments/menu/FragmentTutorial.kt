package com.example.adygall2.presentation.fragments.menu

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.adygall2.R
import com.example.adygall2.databinding.FragmentAppTutorialBinding
import com.example.adygall2.presentation.view_model.AppTutorialViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentTutorial: Fragment(R.layout.fragment_app_tutorial) {

    private var _binding: FragmentAppTutorialBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<AppTutorialViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppTutorialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            viewModel.bindGif(imageView = firstQuestionTypeGif, gifId = R.drawable.example_gif)
            viewModel.bindGif(imageView = secondQuestionTypeGif, gifId = R.drawable.example_gif)
            viewModel.bindGif(imageView = thirdQuestionTypeGif, gifId = R.drawable.example_gif)
            viewModel.bindGif(imageView = fourthQuestionTypeGif, gifId = R.drawable.example_gif)
            viewModel.bindGif(imageView = fiveQuestionTypeGif, gifId = R.drawable.example_gif)
            viewModel.bindGif(imageView = sixthQuestionTypeGif, gifId = R.drawable.example_gif)
            viewModel.bindGif(imageView = seventhQuestionTypeGif, gifId = R.drawable.example_gif)
            viewModel.bindGif(imageView = eightQuestionTypeGif, gifId = R.drawable.example_gif)
            viewModel.bindGif(imageView = ninthQuestionTypeGif, gifId = R.drawable.example_gif)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}