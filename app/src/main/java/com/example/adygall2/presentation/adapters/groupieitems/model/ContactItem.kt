package com.example.adygall2.presentation.adapters.groupieitems.model

import androidx.annotation.DrawableRes

data class ContactItem(
    val company: String,
    val email: String,
    val telNumber: String,
    @DrawableRes val companyImageId: Int
)
