package com.example.adygall2.data.models

import android.content.ContentResolver
import android.content.Context
import android.content.ContextWrapper
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources

class ResourceProvider(
    private val appContext: Context
) {
    fun getString(@StringRes stringId: Int) = appContext.getString(stringId)
    fun getString(@StringRes stringId: Int, vararg args: Any?) = appContext.resources.getString(stringId, *args).toString()
    fun getDrawable(@DrawableRes drawableId: Int) = AppCompatResources.getDrawable(appContext, drawableId)
    val provideContentResolver: ContentResolver get() = appContext.contentResolver
    val contextWrapper get() = ContextWrapper(appContext)
}