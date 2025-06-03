package com.bhushan.android.domain.usecase

import com.bhushan.android.domain.model.EmotionResult
import com.bhushan.android.domain.model.ImageData
import com.bhushan.android.domain.repository.EmotionRepository

class DetectEmotionUseCase(
    private val emotionRepository: EmotionRepository
) {
    suspend operator fun invoke(imageData: ImageData, selectedModel: MLType): EmotionResult {
        return when (selectedModel) {
            MLType.TF_LITE -> emotionRepository.detectEmotionTFLite(imageData)
            MLType.ONNX -> emotionRepository.detectEmotionOnnx(imageData)
        }
    }
}

enum class MLType {
    TF_LITE, ONNX
}