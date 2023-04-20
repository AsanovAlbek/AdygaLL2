package com.example.adygall2.presentation.view_model

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.adygall2.R
import com.example.adygall2.data.models.ResourceProvider
import com.example.adygall2.domain.usecases.UserSettingsUseCase
import com.example.adygall2.presentation.fragments.menu.FragmentEditUserDirections
import com.example.adygall2.presentation.model.UserProfileState
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditUserViewModel(
    private val resourceProvider: ResourceProvider,
    private val userSettingsUseCase: UserSettingsUseCase,
    private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val userChanges = MutableLiveData<UserProfileState>()
    val changes: LiveData<UserProfileState> get() = userChanges
    private var current = UserProfileState()

    fun initViewModel() {
        viewModelScope.launch {
            withContext(mainDispatcher) {
                val user = userSettingsUseCase.userInfo()
                current = UserProfileState(
                    name = user.name,
                    photo = userSettingsUseCase.photo(resourceProvider)
                )
                userChanges.value = current
            }
        }

    }

    fun updateAvatar(bitmap: Bitmap? = null, uri: Uri = Uri.EMPTY) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val image = bitmap ?: ImageDecoder.decodeBitmap(
                    ImageDecoder.createSource(
                        resourceProvider.provideContentResolver, uri
                    )
                )
                withContext(mainDispatcher) {
                    current = current.copy(photo = image)
                    userChanges.value = current
                }
            }
        }
    }

    fun acceptChanges(view: View, navController: NavController) {
        viewModelScope.launch {
            withContext(mainDispatcher) {
                userSettingsUseCase.apply {
                    updateUserInfo(name = current.name)
                    savePhoto(
                        image = current.photo ?: resourceProvider.getDrawable(R.drawable.default_avatar)!!.toBitmap(),
                        resourceProvider = resourceProvider
                    )
                }
                val action = FragmentEditUserDirections.actionEditUserProfileToUserProfile()
                navController.navigate(action)
                Snackbar.make(
                    view,
                    R.string.change_success,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun updateUserName(userInputText: String) {
        viewModelScope.launch {
            withContext(mainDispatcher) {
                current = current.copy(name = userInputText.trim().replace("\n", ""))
                userChanges.value = current
            }
        }
    }
}