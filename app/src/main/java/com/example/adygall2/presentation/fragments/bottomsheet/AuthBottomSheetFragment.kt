package com.example.adygall2.presentation.fragments.bottomsheet

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.adygall2.R
import com.example.adygall2.databinding.AuthBottomSheetBinding
import com.example.adygall2.presentation.fragments.snackBar
import com.example.adygall2.presentation.view_model.AuthViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthBottomSheetFragment: BottomSheetDialogFragment() {

    private var _binding: AuthBottomSheetBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<AuthViewModel>()
    private lateinit var cameraAction: ActivityResultLauncher<Void>
    private lateinit var galleryAction: ActivityResultLauncher<String>
    private lateinit var cameraPermission: ActivityResultLauncher<String>
    private lateinit var galleryPermission: ActivityResultLauncher<String>

    companion object {
        const val TAG = "BottomSheetTag"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cameraAction = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            viewModel.savePhotoInCache(image = it)
            dialog?.cancel()
        }

        galleryAction = registerForActivityResult(ActivityResultContracts.GetContent()) {
                viewModel.savePhotoInCache(uri = it)
                dialog?.cancel()
        }

        cameraPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            granted -> if (granted) {
                cameraAction.launch(null)
            }
        }

        galleryPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            granted -> if (granted) {
                galleryAction.launch(getString(R.string.type_image))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AuthBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            makePhotoButton.setOnClickListener {
                cameraAction()
            }

            pickPhotoFromGalleryButton.setOnClickListener {
                galleryAction()
            }
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
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED
        if (isGalleryGranted) {
            galleryAction.launch(getString(R.string.type_image))
        } else {
            galleryPermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    override fun onDestroyView() {
        galleryAction.unregister()
        cameraAction.unregister()
        _binding = null
        super.onDestroyView()
    }
}