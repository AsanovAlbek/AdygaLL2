package com.example.adygall2.presentation.model

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class DialogState(
    @DrawableRes val iconId: Int = 0,
    val rootVisible: Boolean = false,
    val viewPagerEnabled: Boolean = true,
    val answerButtonEnabled: Boolean = false,
    @StringRes val accuracy: Int = 0,
    @ColorRes val accuracyTextColor: Int = 0,
    val rightTextVisible: Boolean = false,
    val correctAnswer: String = "",
    val correctAnswerVisible: Boolean = false,
    val buttonAction: () -> Unit = {}
)
