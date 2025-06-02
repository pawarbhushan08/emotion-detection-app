package com.bhushan.android.emotiondetectionapp

import android.app.Application
import com.bhushan.android.emotiondetectionapp.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class EmotionDetectionApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@EmotionDetectionApplication)
            modules(appModules)
        }
    }
}