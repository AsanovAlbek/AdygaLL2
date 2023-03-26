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
    fun getPhotoFromCache(): Bitmap {
        val directory =
            resourceProvider.contextWrapper.getDir(
                resourceProvider.getString(R.string.user_avatar),
                Context.MODE_PRIVATE
            )
        val saveFile = File(directory, "thumbnail.jpeg")

        if (saveFile.exists()) {
            FileInputStream(saveFile).use { inputStream ->
                return BitmapFactory.decodeStream(inputStream)
            }
        } else {
            return resourceProvider.getBitmap(R.drawable.default_avatar)!!.toBitmap()
        }
    }

    override fun onCleared() {
        userSettingsUseCase.updateUserInfo(userLastFragment = RESULT_SCREEN)
        super.onCleared()
    }
}