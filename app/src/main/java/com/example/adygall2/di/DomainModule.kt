package com.example.adygall2.di

import com.example.adygall2.domain.usecases.*
import org.koin.dsl.module

val domainModule = module {
    factory { AnswersByTaskIdUseCase() }
    factory { GetAllOrdersUseCase() }
    factory { TasksByOrdersUseCase() }
    factory { SourceInteractor(get()) }
    factory { GetComplexAnswerUseCase() }
}