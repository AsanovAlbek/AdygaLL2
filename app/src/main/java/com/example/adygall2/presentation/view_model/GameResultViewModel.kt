package com.example.adygall2.presentation.view_model

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.example.adygall2.data.models.ResourceProvider
import com.example.adygall2.domain.usecases.UserUseCase

class GameResultViewModel(
    private val resourceProvider: ResourceProvider,
    private val userUseCase: UserUseCase
): ViewModel() {
    fun getPhotoFromCache(): Bitmap = userUseCase.getUserImage(resourceProvider = resourceProvider)
}