package com.example.adygall2.di

import com.example.adygall2.domain.usecases.*
import org.koin.dsl.module

val domainModule = module {
    factory { AnswerUseCase(get()) }
    factory { OrderUseCase(get()) }
    factory { PictureUseCase(get()) }
    factory { TaskUseCase(get()) }
    factory { SoundUseCase(get()) }
}