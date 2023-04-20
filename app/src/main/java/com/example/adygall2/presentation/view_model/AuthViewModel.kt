package com.example.adygall2.presentation.view_model

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.adygall2.data.models.ResourceProvider
import com.example.adygall2.data.models.settings.UserInfo
import com.example.adygall2.domain.usecases.UserSettingsUseCase
import com.example.adygall2.presentation.const.LastNavigationPage.SIGN_UP_SCREEN

class AuthViewModel(
    private val resourceProvider: ResourceProvider,
    private val userSettingsUseCase: UserSettingsUseCase
): ViewModel() {

    companion object {
        private const val TAG = "AuthVM"
    }

    val savedUser: UserInfo get() = userSettingsUseCase.userInfo()

    fun logInUser(userName: String) = userSettingsUseCase.updateUserInfo(
            name = userName.replace("\n", ""),
            userIsSignUp = true
        )

    fun savePhotoInCache(image: Bitmap? = null, uri: Uri = Uri.EMPTY) {
        userSettingsUseCase.savePhoto(image = image, uri = uri, resourceProvider = resourceProvider)
    }

    fun getPhotoFromCache(): Bitmap {
        return userSettingsUseCase.photo(resourceProvider)
    }
}