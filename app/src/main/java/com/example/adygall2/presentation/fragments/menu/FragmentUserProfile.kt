package com.example.adygall2.presentation.fragments.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.adygall2.R
import com.example.adygall2.databinding.FragmentUserProfileBinding
import com.example.adygall2.presentation.model.UserProfileState
import com.example.adygall2.presentation.view_model.UserProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentUserProfile: Fragment(R.layout.fragment_user_profile) {

    private var _userProfileBinding: FragmentUserProfileBinding? = null
    private val userProfileBinding get() = _userProfileBinding!!
    private val viewModel by viewModel<UserProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _userProfileBinding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return userProfileBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.profile.observe(viewLifecycleOwner, ::observeUser)
        userProfileBinding.changeProfile.setOnClickListener {
            findNavController().navigate(R.id.editUserProfile)
        }
    }

    private fun observeUser(state: UserProfileState) {
        userProfileBinding.apply {
            avatar.setImageBitmap(state.photo)
            name.text = state.name
            wordsProgress.text = "${wordsProgress.text} ${state.learnedWordsCount}"
            weekHours.text = "${weekHours.text} ${state.weekPlayingHours}"
            globalHours.text = "${globalHours.text} ${state.globalPlayingHours}"
            levelProgress.text = "${levelProgress.text} ${state.levelProgress}"
            lessonProgress.text = "${lessonProgress.text} ${state.lessonProgress}"
        }
    }

    override fun onDestroyView() {
        _userProfileBinding = null
        super.onDestroyView()
    }
}