package com.bhushan.android.emotiondetectionapp.di

import com.bhushan.android.presentation.camera.di.presentationModule


val appModules: List<org.koin.core.module.Module> =
    listOf(presentationModule)