package com.example.adygall2.presentation.view_model

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.util.LruCache
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adygall2.R
import com.example.adygall2.data.models.ResourceProvider
import com.example.adygall2.data.models.settings.UserInfo
import com.example.adygall2.domain.usecases.UserSettingsUseCase
import com.example.adygall2.presentation.const.LastNavigationPage.SIGN_UP_SCREEN
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel(
    private val resourceProvider: ResourceProvider,
    private val userSettingsUseCase: UserSettingsUseCase
): ViewModel() {

    companion object {
        private const val TAG = "AuthVM"
    }

    val savedUser: UserInfo get() = userSettingsUseCase.userInfo()

    fun logInUser(userName: String) = userSettingsUseCase.updateUserInfo(
            name = userName,
            userIsSignUp = true
        )

    fun savePhotoInCache(image: Bitmap? = null, uri: Uri = Uri.EMPTY) {

        val savedImage = image ?: ImageDecoder.decodeBitmap(
            ImageDecoder.createSource(
                resourceProvider.provideContentResolver, uri
            )
        )

        val directory =
            resourceProvider.contextWrapper.getDir(
                resourceProvider.getString(R.string.user_avatar),
                Context.MODE_PRIVATE
            )
        if (!directory.exists()) {
            directory.mkdir()
        }

        val saveFile = File(directory, "thumbnail.jpeg")
        FileOutputStream(saveFile).use { outputStream ->
            savedImage.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        }
    }

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
        userSettingsUseCase.updateUserInfo(userLastFragment = SIGN_UP_SCREEN)
        super.onCleared()
    }
}