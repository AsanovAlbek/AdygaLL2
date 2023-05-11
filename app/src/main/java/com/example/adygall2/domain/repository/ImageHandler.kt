package com.example.adygall2.domain.repository

import android.graphics.Bitmap
import android.net.Uri
import com.example.adygall2.data.models.ResourceProvider

interface ImageHandler {
    fun photo(resourceProvider: ResourceProvider): Bitmap
    fun savePhoto(resourceProvider: ResourceProvider, image: Bitmap?, uri: Uri)
}