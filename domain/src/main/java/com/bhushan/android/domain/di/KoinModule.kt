package com.bhushan.android.domain.di

import com.bhushan.android.domain.usecase.DetectEmotionUseCase
import org.koin.dsl.module

val domainModule = module {
    single { DetectEmotionUseCase(get()) }
}