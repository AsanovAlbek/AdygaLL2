package com.example.adygall2.di

import com.example.adygall2.di.DiConst.IO_DISPATCHER
import com.example.adygall2.di.DiConst.MAIN_DISPATCHER
import com.example.adygall2.presentation.view_model.AppTutorialViewModel
import com.example.adygall2.presentation.view_model.AuthViewModel
import com.example.adygall2.presentation.view_model.EditUserViewModel
import com.example.adygall2.presentation.view_model.GameResultViewModel
import com.example.adygall2.presentation.view_model.GameViewModel
import com.example.adygall2.presentation.view_model.HomeViewModel
import com.example.adygall2.presentation.view_model.MainViewModel
import com.example.adygall2.presentation.view_model.UserProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.core.scope.get
import org.koin.dsl.module

val presentationModule = module {
    viewModel{
        GameViewModel (
            answersByTaskIdUseCase = get(),
            getComplexAnswerUseCase = get(),
            answerFormatterDelegate = get(),
            sourceInteractor = get(),
            resourceProvider = get(),
            userUseCase = get(),
            soundsPlayer = get(),
            mainDispatcher = get(named(MAIN_DISPATCHER)),
            tasksByLessonUseCase = get(),
            ioDispatcher = get(named(IO_DISPATCHER))
        )
    }

    viewModel {
        AuthViewModel(
            userUseCase = get(),
            resourceProvider = get(),
            ioDispatcher = get(named(IO_DISPATCHER)),
            mainDispatcher = get(named(MAIN_DISPATCHER))
        )
    }

    viewModel {
        HomeViewModel(
            userUseCase = get(),
            resourceProvider = get(),
            tasksByLessonUseCase = get(),
            mainDispatcher = get(named(MAIN_DISPATCHER)),
            ioDispatcher = get(named(IO_DISPATCHER))
        )
    }

    viewModel {
        GameResultViewModel(
            resourceProvider = get(),
            userUseCase = get()
        )
    }

    viewModel {
        MainViewModel(
            resourceProvider = get(),
            mainDispatcher = get(named(MAIN_DISPATCHER)),
            ioDispatcher = get(named(IO_DISPATCHER)),
            userUseCase = get()
        )
    }

    viewModel {
        EditUserViewModel(
            resourceProvider = get(),
            mainDispatcher = get(named(MAIN_DISPATCHER)),
            userUseCase = get(),
            ioDispatcher = get(named(IO_DISPATCHER))
        )
    }

    viewModel {
        UserProfileViewModel(
            resourceProvider = get(),
            userUseCase = get(),
            mainDispatcher = get(named(MAIN_DISPATCHER)),
            ioDispatcher = get(named(IO_DISPATCHER))
        )
    }

    viewModel {
        AppTutorialViewModel(
            mainDispatcher = get(named(MAIN_DISPATCHER))
        )
    }
}