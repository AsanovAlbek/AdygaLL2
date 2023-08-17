package com.example.adygall2.domain.usecases

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.core.content.edit
import com.example.adygall2.data.local.PrefConst
import com.example.adygall2.data.models.ResourceProvider
import com.example.adygall2.data.room.userbase.ProgressItem
import com.example.adygall2.domain.model.User
import com.example.adygall2.domain.repository.ImageHandler
import com.example.adygall2.domain.repository.Repository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserUseCase(
    private val repository: Repository,
    private val imageHandler: ImageHandler,
    private val ioDispatcher: CoroutineDispatcher,
    private val isUserLogIn: SharedPreferences
) {
    fun isUserLogIn(): Boolean = isUserLogIn.getBoolean(PrefConst.IS_USER_SIGN_UP, false)

    fun getUserImage(resourceProvider: ResourceProvider) =
        imageHandler.photo(resourceProvider = resourceProvider)

    fun saveUserImage(
        resourceProvider: ResourceProvider,
        image: Bitmap? = null,
        uri: Uri = Uri.EMPTY
    ) = imageHandler.savePhoto(
        resourceProvider = resourceProvider,
        image = image,
        uri = uri
    )

    suspend fun getUser(): User = withContext(ioDispatcher) {
        return@withContext repository.getUser()
    }


    suspend fun updateUser(user: User) = withContext(ioDispatcher) {
        isUserLogIn.edit {
            putBoolean(PrefConst.IS_USER_SIGN_UP, true)
        }
        return@withContext repository.updateUser(user)
    }

    suspend fun addCompletedLesson(level: Int, lesson: Int) {
        withContext(ioDispatcher) {
            val user = getUser()
            Log.i("user", "completed lessons before = ${user.learnedWords}")
            withContext(Dispatchers.Main) {
                user.learningProgressSet.add(ProgressItem(level = level, lesson = lesson))
            }
            Log.i("user", "completed lessons after = ${user.learnedWords}")
            updateUser(user = user)
        }
    }

    suspend fun isUserExist() = withContext(ioDispatcher) {
        return@withContext repository.isUserExist()
    }
}