package com.example.adygall2.presentation.fragments.menu

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.adygall2.R
import com.example.adygall2.databinding.FragmentEditUserBinding
import com.example.adygall2.presentation.activities.MainActivity
import com.example.adygall2.presentation.model.UserProfileState
import com.example.adygall2.presentation.view_model.EditUserViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentEditUser : Fragment(R.layout.fragment_edit_user) {
    private var _editUserBinding: FragmentEditUserBinding? = null
    private val editUserBinding get() = _editUserBinding!!
    private val viewModel by viewModel<EditUserViewModel>()

    private lateinit var cameraAction: ActivityResultLauncher<Void>
    private lateinit var galleryAction: ActivityResultLauncher<String>
    private lateinit var cameraPermission: ActivityResultLauncher<String>
    private lateinit var galleryPermission: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerToPermissions()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _editUserBinding = FragmentEditUserBinding.inflate(inflater, container, false)
        return editUserBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observe()
        viewModel.initViewModel()
        editUserBinding.apply {
            cameraButton.setOnClickListener { cameraAction() }
            galleryButton.setOnClickListener { galleryAction() }
            saveButton.setOnClickListener {
                viewModel.updateUserName(editName.text.toString())
                viewModel.acceptChanges(requireView(), findNavController())
                (requireActivity() as MainActivity).updateUserStates()
            }
        }
    }

    private fun registerToPermissions() {
        cameraAction =
            registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmapImage ->
                viewModel.updateAvatar(bitmap = bitmapImage)
            }

        galleryAction =
            registerForActivityResult(ActivityResultContracts.GetContent()) { contentUri ->
                viewModel.updateAvatar(uri = contentUri)
            }

        cameraPermission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) {
                    cameraAction.launch(null)
                }
            }

        galleryPermission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) {
                    galleryAction.launch(getString(R.string.type_image))
                }
            }
    }

    private fun observe() {
        viewModel.changes.observe(viewLifecycleOwner, ::setupChanges)
    }

    private fun setupChanges(state: UserProfileState) {
        editUserBinding.apply {
            avatar.setImageBitmap(state.photo)
            editName.setText(state.name)
        }
    }

    private fun cameraAction() {
        val isCameraGranted =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED
        if (isCameraGranted) {
            cameraAction.launch(null)
        } else {
            cameraPermission.launch(Manifest.permission.CAMERA)
        }
    }

    private fun galleryAction() {
        val isGalleryGranted =
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        if (isGalleryGranted) {
            galleryAction.launch(getString(R.string.type_image))
        } else {
            galleryPermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }


    override fun onDestroyView() {
        _editUserBinding = null
        super.onDestroyView()
    }
}