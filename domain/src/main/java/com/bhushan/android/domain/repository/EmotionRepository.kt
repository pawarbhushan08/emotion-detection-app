package com.bhushan.android.domain.repository

import com.bhushan.android.domain.model.EmotionResult
import com.bhushan.android.domain.model.ImageData

interface EmotionRepository {
    suspend fun detectEmotionTFLite(imageData: ImageData): EmotionResult
    suspend fun detectEmotionOnnx(imageData: ImageData): EmotionResult
}