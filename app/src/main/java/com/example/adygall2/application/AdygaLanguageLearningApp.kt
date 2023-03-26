package com.example.adygall2.application

import android.app.Application
import com.example.adygall2.di.dataModule
import com.example.adygall2.di.domainModule
import com.example.adygall2.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AdygaLanguageLearningApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AdygaLanguageLearningApp)
            modules(dataModule, domainModule, presentationModule)
        }
    }
}