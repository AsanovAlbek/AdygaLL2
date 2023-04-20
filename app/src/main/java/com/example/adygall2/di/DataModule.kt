package com.example.adygall2.di

import android.content.Context
import android.media.MediaPlayer
import com.example.adygall2.data.delegate.AnswerFormatter
import com.example.adygall2.data.delegate.AnswerFormatterImpl
import com.example.adygall2.data.local.PrefConst
import com.example.adygall2.data.models.FilesHandler
import com.example.adygall2.data.models.ResourceProvider
import com.example.adygall2.data.models.SoundsPlayer
import com.example.adygall2.data.models.settings.UserSettings
import com.example.adygall2.data.repository.RepositoryImpl
import com.example.adygall2.data.room.dao.AnswerDao
import com.example.adygall2.data.room.dao.OrderDao
import com.example.adygall2.data.room.dao.PictureDao
import com.example.adygall2.data.room.dao.SoundEffectDao
import com.example.adygall2.data.room.dao.SoundsDao
import com.example.adygall2.data.room.dao.TaskDao
import com.example.adygall2.data.room.database.GameBase
import com.example.adygall2.domain.repository.Repository
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module

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
    factory<SoundsPlayer>{ SoundsPlayer(
        androidApplication().applicationContext,
        FilesHandler(androidApplication().applicationContext),
        MediaPlayer()
    ) }
    factory {
        ResourceProvider(androidApplication().applicationContext)
    }

    /** Хранение здоровья пользователя */
    single(named(PrefConst.USER_HP)) {
        androidApplication().applicationContext.getSharedPreferences(PrefConst.USER_HP, Context.MODE_PRIVATE)
    }

    /** Хранение монет пользователя */
    single(named(PrefConst.USER_EXP)) {
        androidApplication().applicationContext.getSharedPreferences(PrefConst.USER_EXP, Context.MODE_PRIVATE)
    }

    /** Хранение даты и времени выхода из приложения */
    single(named(PrefConst.DATE)) {
        androidApplication().applicationContext.getSharedPreferences(PrefConst.DATE, Context.MODE_PRIVATE)
    }

    single(named(PrefConst.USER_PROGRESS)) {
        androidApplication().applicationContext.getSharedPreferences(PrefConst.USER_PROGRESS, Context.MODE_PRIVATE)
    }

    /** Выученные слова */
    single(named(PrefConst.LEARNED_WORDS)) {
        androidApplication().applicationContext.getSharedPreferences(PrefConst.LEARNED_WORDS, Context.MODE_PRIVATE)
    }

    /** Пользовательское имя */
    single(named(PrefConst.USER_NAME)) {
        androidApplication().applicationContext.getSharedPreferences(PrefConst.USER_NAME, Context.MODE_PRIVATE)
    }

    single(named(PrefConst.IS_USER_SIGN_UP)) {
        androidApplication().applicationContext.getSharedPreferences(PrefConst.IS_USER_SIGN_UP, Context.MODE_PRIVATE)
    }

    single(named(PrefConst.LAST_USER_FRAGMENT)) {
        androidApplication().applicationContext.getSharedPreferences(PrefConst.LAST_USER_FRAGMENT, Context.MODE_PRIVATE)
    }

    single<UserSettings> { UserSettings }
}