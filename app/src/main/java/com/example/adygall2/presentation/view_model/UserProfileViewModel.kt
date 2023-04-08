package com.example.adygall2.presentation.view_model

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
import com.example.adygall2.presentation.fragments.menu.FragmentUserProfileDirections
import com.example.adygall2.presentation.model.UserProfileState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserProfileViewModel(
    private val resourceProvider: ResourceProvider,
    private val userSettingsUseCase: UserSettingsUseCase,
    private val mainDispatcher: CoroutineDispatcher
): ViewModel() {
    private val userProfile = MutableLiveData<UserProfileState>()
    val profile: LiveData<UserProfileState> get() = userProfile
    private var currentProfile =
        UserProfileState(photo = resourceProvider.getBitmap(R.drawable.default_avatar)!!.toBitmap())

    init {
        viewModelScope.launch {
            withContext(mainDispatcher) {
                val user = userSettingsUseCase.userInfo()
                currentProfile = UserProfileState(
                    name = user.name,
                    photo = userSettingsUseCase.photo(resourceProvider),
                    learnedWordsCount = user.learnedWords.count(),
                    levelProgress = user.learningProgressSet.distinctBy { it.level }.size,
                    lessonProgress = user.learningProgressSet.size,
                    globalPlayingHours = 1,
                    weekPlayingHours = 1
                )
                userProfile.value = currentProfile
            }
        }
    }

    fun editUserProfileClick(navController: NavController) {
        val action = FragmentUserProfileDirections.actionUserProfileToEditUserProfile()
        navController.navigate(action)
    }
}