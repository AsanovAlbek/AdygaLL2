package com.example.adygall2.di

import com.example.adygall2.data.delegate.AnswerFormatter
import com.example.adygall2.data.delegate.AnswerFormatterImpl
import com.example.adygall2.data.models.SoundsPlayer
import com.example.adygall2.data.repository.RepositoryImpl
import com.example.adygall2.data.room.dao.*
import com.example.adygall2.data.room.database.GameBase
import com.example.adygall2.domain.repository.Repository
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dataModule = module {
    single { GameBase.buildDatabase(androidApplication()) }

    single { get<GameBase>().getAnswerDao() }
    single { get<GameBase>().getOrderDao() }
    single { get<GameBase>().getPictureDao() }
    single { get<GameBase>().getTaskDao() }
    single { get<GameBase>().getSoundsDao() }
    single { get<GameBase>().getSoundsEffectDao() }

    factory<Repository> {
        RepositoryImpl(
            context = androidApplication(),
            answerDao = get() as AnswerDao,
            orderDao = get() as OrderDao,
            pictureDao = get() as PictureDao,
            taskDao = get() as TaskDao,
            soundsDao = get() as SoundsDao,
            soundEffectDao = get() as SoundEffectDao
        )
    }
    factory<AnswerFormatter> { AnswerFormatterImpl() }
    factory<SoundsPlayer>{ SoundsPlayer(androidApplication()) }
}