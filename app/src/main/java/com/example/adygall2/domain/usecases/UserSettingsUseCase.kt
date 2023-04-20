package com.example.adygall2.domain.usecases

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.core.graphics.drawable.toBitmap
import com.example.adygall2.R
import com.example.adygall2.data.models.ResourceProvider
import com.example.adygall2.data.models.settings.ProgressItem
import com.example.adygall2.data.models.settings.UserSettings
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class UserSettingsUseCase(
    private val userSettings: UserSettings
    ) {
    fun userInfo() = userSettings.userInfo()

    fun updateUserInfo(
        userHp: Int? = null,
        userCoins: Int? = null,
        name: String? = null,
        userIsSignUp: Boolean? = null,
        userLastOnlineTime: Long? = null,
        learnedWords: Set<String>? = null,
        userLearningProgressSet: Set<ProgressItem>? = null,
        userLastFragment: Int? = null
    ) = userSettings.updateUserInfo(
            userHp = userHp,
            userCoins = userCoins,
            name = name,
            userIsSignUp = userIsSignUp,
            userLastOnlineTime = userLastOnlineTime,
            learnedWords = learnedWords,
            userLearningProgressSet = userLearningProgressSet,
            lastSavedFragment = userLastFragment
        )

    fun addLessonToCompletedLessons(level: Int, lesson: Int) = userSettings.addCompletedLesson(level, lesson)

    fun photo(resourceProvider: ResourceProvider): Bitmap {
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
            return resourceProvider.getDrawable(R.drawable.default_avatar)!!.toBitmap()
        }
    }

    fun savePhoto(resourceProvider: ResourceProvider, image: Bitmap? = null, uri: Uri = Uri.EMPTY) {
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
}