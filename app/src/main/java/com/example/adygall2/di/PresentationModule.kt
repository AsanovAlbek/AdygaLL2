package com.example.adygall2.di

import com.example.adygall2.presentation.view_model.GameViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel{
        GameViewModel (
            answersByTaskIdUseCase = get(),
            getAllOrdersUseCase = get(),
            tasksByOrdersUseCase = get(),
            getComplexAnswerUseCase = get(),
            answerFormatterDelegate = get(),
            sourceInteractor = get()
        )
    }
}