package com.example.adygall2.di

import com.example.adygall2.di.DiConst.IO_DISPATCHER
import com.example.adygall2.di.DiConst.MAIN_DISPATCHER
import com.example.adygall2.domain.usecases.AnswersByTaskIdUseCase
import com.example.adygall2.domain.usecases.GetAllOrdersUseCase
import com.example.adygall2.domain.usecases.GetComplexAnswerUseCase
import com.example.adygall2.domain.usecases.SourceInteractor
import com.example.adygall2.domain.usecases.TasksByOrdersUseCase
import com.example.adygall2.domain.usecases.UserSettingsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

val domainModule = module {

    single<CoroutineDispatcher>(named(IO_DISPATCHER)) { Dispatchers.IO }
    single<CoroutineDispatcher>(named(MAIN_DISPATCHER)) { Dispatchers.Main }

    factory { AnswersByTaskIdUseCase(get(), get(named(IO_DISPATCHER))) }
    factory { GetAllOrdersUseCase(get(), get(named(IO_DISPATCHER))) }
    factory { TasksByOrdersUseCase(get(), get(named(IO_DISPATCHER))) }
    factory { SourceInteractor(get(), get(named(IO_DISPATCHER))) }
    factory { GetComplexAnswerUseCase(get(), get(named(IO_DISPATCHER))) }
    factory { UserSettingsUseCase(get()) }
}