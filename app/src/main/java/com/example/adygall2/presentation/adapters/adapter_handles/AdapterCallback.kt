package com.example.adygall2.presentation.adapters.adapter_handles

import com.example.adygall2.data.db_models.Answer

interface AdapterCallback {
    fun change(isFirstAdapter: Boolean, item : String, position: Int)
}