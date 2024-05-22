package com.example.adygall2.di

import com.example.adygall2.data.local.PrefConst
import com.example.adygall2.di.DiConst.IO_DISPATCHER
import com.example.adygall2.di.DiConst.MAIN_DISPATCHER
import com.example.adygall2.domain.usecases.AnswersByTaskIdUseCase
import com.example.adygall2.domain.usecases.GetAllOrdersUseCase
import com.example.adygall2.domain.usecases.GetComplexAnswerUseCase
import com.example.adygall2.domain.usecases.SourceInteractor
import com.example.adygall2.domain.usecases.TasksByLessonUseCase
import com.example.adygall2.domain.usecases.TasksByOrdersUseCase
import com.example.adygall2.domain.usecases.UserUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

val domainModule = module {

    single<CoroutineDispatcher>(named(IO_DISPATCHER)) { Dispatchers.IO }
    single<CoroutineDispatcher>(named(MAIN_DISPATCHER)) { Dispatchers.Main }

    factory { AnswersByTaskIdUseCase(repository = get(), ioDispatcher = get(named(IO_DISPATCHER))) }
    factory { GetAllOrdersUseCase(repository = get(), ioDispatcher = get(named(IO_DISPATCHER))) }
    factory { TasksByOrdersUseCase(repository = get(), ioDispatcher = get(named(IO_DISPATCHER))) }
    factory { SourceInteractor(repository = get(), ioDispatcher = get(named(IO_DISPATCHER))) }
    factory {
        GetComplexAnswerUseCase(
            repository = get(),
            ioDispatcher = get(named(IO_DISPATCHER))
        )
    }
    factory {
        UserUseCase(
            repository = get(),
            imageHandler = get(),
            ioDispatcher = get(named(IO_DISPATCHER)),
            isUserLogIn = get(named(PrefConst.IS_USER_SIGN_UP))
        )
    }
    factory { TasksByLessonUseCase(repository = get(), ioDispatcher = get(named(IO_DISPATCHER))) }
}