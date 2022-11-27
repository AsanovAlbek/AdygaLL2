package com.example.adygall2.presentation.adapters.groupieitems.model

import androidx.annotation.ColorRes
import com.example.adygall2.R

data class HomeColorsTheme(
    @ColorRes val backgroundColor: Int = R.color.pink_circle,
    @ColorRes val progressColor: Int = R.color.blue
) {
    fun paint(background: Int) = when(background) {
            R.color.pink_circle -> newColorTheme(background, R.color.blue)
            R.color.orange_warm -> newColorTheme(background, R.color.red_warm)
            R.color.cool_green -> newColorTheme(background, R.color.snow_furry)
            R.color.brink_pink -> newColorTheme(background, R.color.carnation_pink)
            R.color.cornflower_blue -> newColorTheme(background, R.color.hawkes_blue)
            else -> newColorTheme(background, R.color.blue)
        }

    private fun newColorTheme(backgroundColor: Int, progressColor: Int) =
        HomeColorsTheme(
            backgroundColor = backgroundColor,
            progressColor =  progressColor
        )
}