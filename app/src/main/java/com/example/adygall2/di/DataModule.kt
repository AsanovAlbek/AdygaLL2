package com.example.adygall2.di

import android.content.Context
import android.media.MediaPlayer
import com.example.adygall2.data.delegate.AnswerFormatter
import com.example.adygall2.data.delegate.AnswerFormatterImpl
import com.example.adygall2.data.local.PrefConst
import com.example.adygall2.data.models.FilesHandler
import com.example.adygall2.data.models.ResourceProvider
import com.example.adygall2.data.models.SoundsPlayer
import com.example.adygall2.data.models.UserImageHandler
import com.example.adygall2.data.repository.RepositoryImpl
import com.example.adygall2.data.room.gamebase.dao.AnswerDao
import com.example.adygall2.data.room.gamebase.dao.OrderDao
import com.example.adygall2.data.room.gamebase.dao.PictureDao
import com.example.adygall2.data.room.gamebase.dao.SoundEffectDao
import com.example.adygall2.data.room.gamebase.dao.SoundsDao
import com.example.adygall2.data.room.gamebase.dao.TaskDao
import com.example.adygall2.data.room.gamebase.database.GameBase
import com.example.adygall2.data.room.userbase.dao.UserDao
import com.example.adygall2.data.room.userbase.database.UserDataBase
import com.example.adygall2.domain.repository.ImageHandler
import com.example.adygall2.domain.repository.Repository
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataModule = module {
    single { GameBase.buildDatabase(androidApplication().applicationContext) }
    single { UserDataBase.buildDatabase(androidApplication().applicationContext) }

    single { get<GameBase>().getAnswerDao() }
    single { get<GameBase>().getOrderDao() }
    single { get<GameBase>().getPictureDao() }
    single { get<GameBase>().getTaskDao() }
    single { get<GameBase>().getSoundsDao() }
    single { get<GameBase>().getSoundsEffectDao() }
    single { get<UserDataBase>().getUserDao() }

    factory<Repository> {
        RepositoryImpl(
            context = androidApplication().applicationContext,
            answerDao = get() as AnswerDao,
            orderDao = get() as OrderDao,
            pictureDao = get() as PictureDao,
            taskDao = get() as TaskDao,
            soundsDao = get() as SoundsDao,
            soundEffectDao = get() as SoundEffectDao,
            userDao = get() as UserDao
        )
    }
    factory<AnswerFormatter> { AnswerFormatterImpl() }
    factory<SoundsPlayer> {
        SoundsPlayer(
            androidApplication().applicationContext,
            FilesHandler(androidApplication().applicationContext),
            MediaPlayer()
        )
    }
    factory {
        ResourceProvider(androidApplication().applicationContext)
    }
    factory<ImageHandler> {
        UserImageHandler()
    }

    single(named(PrefConst.IS_USER_SIGN_UP)) {
        androidApplication().applicationContext.getSharedPreferences(
            PrefConst.IS_USER_SIGN_UP,
            Context.MODE_PRIVATE
        )
    }
}