package com.example.adygall2.presentation.activities

import androidx.lifecycle.LiveData
import com.example.adygall2.domain.model.User

interface UserChangeListener {
    fun onUserChange(user: User)
    fun getUserHealthLiveData(): LiveData<Int>
    fun setUserHealthLiveData(value: Int)
    fun damage()
}