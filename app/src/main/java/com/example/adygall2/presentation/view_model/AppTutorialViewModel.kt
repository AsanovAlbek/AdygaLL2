package com.example.adygall2.presentation.view_model

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppTutorialViewModel(
    private val mainDispatcher: CoroutineDispatcher
): ViewModel() {
    fun bindGif(imageView: ImageView, @DrawableRes gifId: Int) {
        viewModelScope.launch {
            withContext(mainDispatcher) {
                Glide.with(imageView.context)
                    .asGif()
                    .load(gifId)
                    .into(imageView)
            }
        }
    }
}