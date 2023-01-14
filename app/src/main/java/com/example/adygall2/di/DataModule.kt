package com.example.adygall2.di

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.example.adygall2.data.delegate.AnswerFormatter
import com.example.adygall2.data.delegate.AnswerFormatterImpl
import com.example.adygall2.data.local.PrefConst
import com.example.adygall2.data.models.FilesHandler
import com.example.adygall2.data.models.SoundsPlayer
import com.example.adygall2.data.repository.RepositoryImpl
import com.example.adygall2.data.room.dao.*
import com.example.adygall2.data.room.database.GameBase
import com.example.adygall2.domain.repository.Repository
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.dsl.single

val dataModule = module {
    single { GameBase.buildDatabase(androidApplication().applicationContext) }

    single { get<GameBase>().getAnswerDao() }
    single { get<GameBase>().getOrderDao() }
    single { get<GameBase>().getPictureDao() }
    single { get<GameBase>().getTaskDao() }
    single { get<GameBase>().getSoundsDao() }
    single { get<GameBase>().getSoundsEffectDao() }

    factory<Repository> {
        RepositoryImpl(
            context = androidApplication().applicationContext,
            answerDao = get() as AnswerDao,
            orderDao = get() as OrderDao,
            pictureDao = get() as PictureDao,
            taskDao = get() as TaskDao,
            soundsDao = get() as SoundsDao,
            soundEffectDao = get() as SoundEffectDao
        )
    }
    factory<AnswerFormatter> { AnswerFormatterImpl() }

    single<FilesHandler>{ FilesHandler(androidApplication().applicationContext) }
    single<MediaPlayer> { MediaPlayer() }

    single<SoundsPlayer>{ SoundsPlayer(
        androidApplication().applicationContext,
        mediaPlayer = get(),
        filesHandler = get()
    ) }

    single(named(PrefConst.USER_HP)) {
        androidApplication().applicationContext.getSharedPreferences(PrefConst.USER_HP, Context.MODE_PRIVATE)
    }

    single(named(PrefConst.USER_EXP)) {
        androidApplication().applicationContext.getSharedPreferences(PrefConst.USER_EXP, Context.MODE_PRIVATE)
    }

    single(named(PrefConst.DATE)) {
        androidApplication().applicationContext.getSharedPreferences(PrefConst.DATE, Context.MODE_PRIVATE)
    }

    single(named(PrefConst.LEVEL_PROGRESS)) {
        androidApplication().applicationContext.getSharedPreferences(PrefConst.LEVEL_PROGRESS, Context.MODE_PRIVATE)
    }

    single(named(PrefConst.LESSON_PROGRESS)) {
        androidApplication().applicationContext.getSharedPreferences(PrefConst.LESSON_PROGRESS, Context.MODE_PRIVATE)
    }

    single(named(PrefConst.LEARNED_WORDS)) {
        androidApplication().applicationContext.getSharedPreferences(PrefConst.LEARNED_WORDS, Context.MODE_PRIVATE)
    }
}