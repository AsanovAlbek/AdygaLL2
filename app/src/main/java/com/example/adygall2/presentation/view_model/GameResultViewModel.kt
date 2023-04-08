package com.example.adygall2.presentation.view_model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import com.example.adygall2.R
import com.example.adygall2.data.models.ResourceProvider
import com.example.adygall2.domain.usecases.UserSettingsUseCase
import com.example.adygall2.presentation.const.LastNavigationPage.RESULT_SCREEN
import java.io.File
import java.io.FileInputStream

class GameResultViewModel(
    private val resourceProvider: ResourceProvider,
    private val userSettingsUseCase: UserSettingsUseCase
): ViewModel() {
    fun getPhotoFromCache(): Bitmap = userSettingsUseCase.photo(resourceProvider)

    override fun onCleared() {
        //userSettingsUseCase.updateUserInfo(userLastFragment = RESULT_SCREEN)
        super.onCleared()
    }
}