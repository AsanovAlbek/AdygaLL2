package com.example.adygall2.presentation.view_model

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adygall2.data.models.ResourceProvider
import com.example.adygall2.domain.model.User
import com.example.adygall2.domain.usecases.UserUseCase
import com.example.adygall2.presentation.activities.UserChangeListener
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel(
    private val resourceProvider: ResourceProvider,
    private val userUseCase: UserUseCase,
    private val ioDispatcher: CoroutineDispatcher,
    private val mainDispatcher: CoroutineDispatcher
): ViewModel() {

    companion object {
        private const val TAG = "AuthVM"
    }

    lateinit var savedUser: User

    init {
        viewModelScope.launch {
            withContext(mainDispatcher) {
                savedUser = if (userUseCase.isUserExist()) userUseCase.getUser() else User()
            }
        }
    }

    fun logInUser(userName: String, userChangeListener: UserChangeListener) {
        viewModelScope.launch {
            withContext(mainDispatcher) {
                Log.i("user", "user name = $userName")
                savedUser = savedUser.copy(
                    name = userName,
                    isUserSignUp = true
                )
                Log.i("user", "user is $savedUser")
                userUseCase.updateUser(savedUser)
                userChangeListener.onUserChange(savedUser)
            }
        }
    }

    fun savePhotoInCache(image: Bitmap? = null, uri: Uri = Uri.EMPTY) {
        userUseCase.saveUserImage(image = image, uri = uri, resourceProvider = resourceProvider)
    }

    fun getPhotoFromCache(): Bitmap {
        return userUseCase.getUserImage(resourceProvider = resourceProvider)
    }
}