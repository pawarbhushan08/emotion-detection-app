package com.bhushan.android.data.di

import com.bhushan.android.data.repository.EmotionRepositoryImpl
import com.bhushan.android.domain.repository.EmotionRepository
import org.koin.dsl.module

val dataModule = module {
    single<EmotionRepository> { EmotionRepositoryImpl(get()) }
}