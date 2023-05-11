package com.example.adygall2.presentation.activities

import com.example.adygall2.domain.model.User

interface UserChangeListener {
    fun onUserChange(user: User)
}