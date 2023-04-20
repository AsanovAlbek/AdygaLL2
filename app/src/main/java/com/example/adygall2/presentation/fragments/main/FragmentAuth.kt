package com.example.adygall2.presentation.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment.STYLE_NORMAL
import androidx.navigation.fragment.findNavController
import com.example.adygall2.R
import com.example.adygall2.databinding.FragmentAuthorizeBinding
import com.example.adygall2.presentation.activities.MainActivity
import com.example.adygall2.presentation.fragments.bottomsheet.AuthBottomSheetFragment
import com.example.adygall2.presentation.view_model.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Класс, имплементирующий интерфейс фрагмента
 * Предназначен для взаимодействия с экраном (окном) ввода имени пользователя и пароля
 */

class FragmentAuth : Fragment(R.layout.fragment_authorize) {

    private var _authBinding: FragmentAuthorizeBinding? = null
    private val authBinding get() = _authBinding!!
    private val viewModel by viewModel<AuthViewModel>()
    private var bottomSheet: AuthBottomSheetFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _authBinding = FragmentAuthorizeBinding.inflate(inflater, container, false)
        return authBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initBottomSheet()
        setClickListeners()
        authBinding.authLoginEt.setText(viewModel.savedUser.name)
    }

    override fun onResume() {
        super.onResume()
        authBinding.userAvatar.setImageBitmap(viewModel.getPhotoFromCache())
    }

    private fun initBottomSheet() {
        bottomSheet = AuthBottomSheetFragment().apply {
            setStyle(STYLE_NORMAL, R.style.SheetDialog)
        }
    }

    private fun setClickListeners() {
        authBinding.apply {
            authSingInButton.setOnClickListener {
                viewModel.logInUser(userName = authBinding.authLoginEt.text.toString())
                (requireActivity() as MainActivity).updateUserStates()
                findNavController().navigate(R.id.action_authorize_to_homePage)
            }

            userAvatar.setOnClickListener { openBottomSheet() }
            choosePhotoTv.setOnClickListener { openBottomSheet() }
        }
    }

    private fun openBottomSheet() {
        bottomSheet?.show(childFragmentManager, AuthBottomSheetFragment.TAG)
    }

    override fun onDestroyView() {
        _authBinding = null
        bottomSheet = null
        super.onDestroyView()
    }
}