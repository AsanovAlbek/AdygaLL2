package com.example.adygall2.presentation.view_model

import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adygall2.R
import com.example.adygall2.data.models.ResourceProvider
import com.example.adygall2.domain.usecases.UserUseCase
import com.example.adygall2.presentation.model.UserProfileState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserProfileViewModel(
    private val resourceProvider: ResourceProvider,
    private val userUseCase: UserUseCase,
    private val mainDispatcher: CoroutineDispatcher,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val userProfile = MutableLiveData<UserProfileState>()
    val profile: LiveData<UserProfileState> get() = userProfile
    private var currentProfile =
        UserProfileState(photo = resourceProvider.getDrawable(R.drawable.default_avatar)!!.toBitmap())

    init {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                val user = userUseCase.getUser()
                val levels = user.learningProgressSet.groupBy { it.level }
                    .filter { it.value.size == 15 }.size
                withContext(mainDispatcher) {
                    currentProfile = UserProfileState(
                        name = user.name,
                        photo = userUseCase.getUserImage(resourceProvider),
                        learnedWordsCount = user.learnedWords.count(),
                        levelProgress = levels,
                        lessonProgress = user.learningProgressSet.size,
                        globalPlayingHours = user.globalPlayingTimeInMillis
                    )
                    userProfile.value = currentProfile
                }
            }
        }
    }

    fun millisToDate(millis: Long): String {
        if (millis > 0) {
            val days = millis / (1000 * 60 * 60 * 24)
            val hours = millis / (1000 * 60 * 60) % 24
            val minutes = millis / (1000 * 60) % 60
            return resourceProvider.getString(R.string.global_date_format, days, hours, minutes)
        }
        return resourceProvider.getString(R.string.global_date_format, 0, 0 ,0)
    }
}