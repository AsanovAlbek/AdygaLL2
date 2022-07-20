package com.example.adygall2.di

import com.example.adygall2.presentation.GameViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel{
        GameViewModel (
        answerUseCase = get(),
        orderUseCase = get(),
        pictureUseCase = get(),
        taskUseCase = get(),
        soundUseCase = get()
        )
    }
}