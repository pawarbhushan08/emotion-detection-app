package com.bhushan.android.domain.model

data class ImageData(
    val pixels: FloatArray,
    val width: Int,
    val height: Int,
    val format: ImageFormat
) {
    enum class ImageFormat {
        RGB, GRAYSCALE
    }
}