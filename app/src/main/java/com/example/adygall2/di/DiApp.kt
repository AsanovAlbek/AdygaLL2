package com.example.adygall2.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class DiApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@DiApp)
            modules(dataModule, domainModule, presentationModule)
        }
    }
}