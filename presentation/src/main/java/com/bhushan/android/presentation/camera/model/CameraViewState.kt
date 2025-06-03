package com.bhushan.android.presentation.camera.model

import androidx.camera.core.SurfaceRequest
import com.bhushan.android.domain.usecase.MLType

data class CameraViewState(
    val hasPermission: Boolean = false,
    val isCameraBound: Boolean = false,
    val surfaceRequest: SurfaceRequest? = null,
    val error: String? = null,
    val isLoadings: Boolean = false,
    val emotionResult: String? = null,
    val modelType: ModelType = ModelType.TF_LITE
)

enum class ModelType {
    TF_LITE, ONNX;

    fun mapToMLType(): MLType {
        return when (this) {
            TF_LITE -> MLType.TF_LITE
            ONNX -> MLType.ONNX
        }
    }
}