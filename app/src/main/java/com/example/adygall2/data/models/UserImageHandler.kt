package com.example.adygall2.data.models

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.core.graphics.drawable.toBitmap
import com.example.adygall2.R
import com.example.adygall2.domain.repository.ImageHandler
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class UserImageHandler: ImageHandler {
    override fun photo(resourceProvider: ResourceProvider): Bitmap {
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

    override fun savePhoto(resourceProvider: ResourceProvider, image: Bitmap?, uri: Uri) {
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