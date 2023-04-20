package com.example.adygall2.presentation.view_model

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.example.adygall2.data.models.ResourceProvider
import com.example.adygall2.domain.usecases.UserSettingsUseCase

class GameResultViewModel(
    private val resourceProvider: ResourceProvider,
    private val userSettingsUseCase: UserSettingsUseCase
): ViewModel() {
    fun getPhotoFromCache(): Bitmap = userSettingsUseCase.photo(resourceProvider)
}