package com.example.adygall2.di

import com.example.adygall2.di.DiConst.IO_DISPATCHER
import com.example.adygall2.di.DiConst.MAIN_DISPATCHER
import com.example.adygall2.presentation.view_model.AuthViewModel
import com.example.adygall2.presentation.view_model.EditUserViewModel
import com.example.adygall2.presentation.view_model.GameResultViewModel
import com.example.adygall2.presentation.view_model.GameViewModel
import com.example.adygall2.presentation.view_model.HomeViewModel
import com.example.adygall2.presentation.view_model.MainViewModel
import com.example.adygall2.presentation.view_model.UserProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val presentationModule = module {
    viewModel{
        GameViewModel (
            answersByTaskIdUseCase = get(),
            getComplexAnswerUseCase = get(),
            answerFormatterDelegate = get(),
            sourceInteractor = get(),
            resourceProvider = get(),
            userSettingsUseCase = get(),
            soundsPlayer = get(),
            mainDispatcher = get(named(MAIN_DISPATCHER)),
            ioDispatcher = get(named(IO_DISPATCHER))
        )
    }

    viewModel {
        AuthViewModel(
            userSettingsUseCase = get(),
            resourceProvider = get()
        )
    }

    viewModel {
        HomeViewModel(
            userSettingsUseCase = get(),
            getAllOrdersUseCase = get(),
            tasksByOrdersUseCase = get(),
            resourceProvider = get(),
            mainDispatcher = get(named(MAIN_DISPATCHER)),
            ioDispatcher = get(named(IO_DISPATCHER))
        )
    }

    viewModel {
        GameResultViewModel(
            resourceProvider = get(),
            userSettingsUseCase = get()
        )
    }

    viewModel {
        MainViewModel(
            resourceProvider = get(),
            mainDispatcher = get(named(MAIN_DISPATCHER)),
            ioDispatcher = get(named(IO_DISPATCHER)),
            userSettingsUseCase = get()
        )
    }

    viewModel {
        EditUserViewModel(
            resourceProvider = get(),
            mainDispatcher = get(),
            userSettingsUseCase = get()
        )
    }

    viewModel {
        UserProfileViewModel(
            resourceProvider = get(),
            userSettingsUseCase = get(),
            mainDispatcher = get(named(MAIN_DISPATCHER))
        )
    }
}