package com.bhushan.android.presentation.camera.vm

import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.core.graphics.createBitmap
import androidx.core.graphics.get
import androidx.core.graphics.scale
import com.bhushan.android.domain.model.ImageData
import com.bhushan.android.domain.usecase.DetectEmotionUseCase
import com.bhushan.android.presentation.camera.model.ModelType
import com.bhushan.android.presentation.camera.utils.YuvToRgbConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface DualEmotionListener {
    fun onEmotionDetected(emotion: String)
}

class DualEmotionAnalyser(
    private val detectEmotionUseCase: DetectEmotionUseCase,
    private val listener: DualEmotionListener,
    private val coroutineScope: CoroutineScope,
    private val modelType: ModelType
) : ImageAnalysis.Analyzer {

    override fun analyze(image: ImageProxy) {
        coroutineScope.launch {
            try {
                // Convert ImageProxy to Bitmap
                val bitmap = createBitmap(image.width, image.height)
                YuvToRgbConverter.yuvToRgb(image, bitmap)

                val result = when (modelType) {
                    ModelType.TF_LITE -> {
                        val tfLiteInput = preprocessBitmapToImageDataForTfLite(bitmap)
                        detectEmotionUseCase.invoke(tfLiteInput, modelType.mapToMLType())
                    }

                    ModelType.ONNX -> {
                        val onnxInput = preprocessBitmapToImageDataForOnnx(bitmap)
                        detectEmotionUseCase.invoke(onnxInput, modelType.mapToMLType())
                    }
                }
                withContext(Dispatchers.Default) {
                    listener.onEmotionDetected(result.emotion)
                }
            } catch (e: Exception) {
                Log.e("DualEmotionAnalyser", "Error processing image", e)
                listener.onEmotionDetected("Error")
            } finally {
                image.close()
            }


        }

    }

    private fun preprocessBitmapToImageDataForTfLite(bitmap: Bitmap): ImageData {
        // Example: for TFLite, use grayscale 48x48; for ONNX, use RGB 224x224
        // You can generalize or make this configurable
        val size = 48
        val resized = bitmap.scale(size, size)
        val pixels = FloatArray(size * size)
        for (y in 0 until size) {
            for (x in 0 until size) {
                val color = resized[x, y]
                val r = (color shr 16) and 0xFF
                val g = (color shr 8) and 0xFF
                val b = color and 0xFF
                pixels[y * size + x] = (0.299f * r + 0.587f * g + 0.114f * b) / 255.0f
            }
        }
        return ImageData(pixels, size, size, ImageData.ImageFormat.GRAYSCALE)
    }

    private fun preprocessBitmapToImageDataForOnnx(bitmap: Bitmap): ImageData {
        val width = 224
        val height = 224
        val resized = bitmap.scale(width, height)
        val pixels = FloatArray(width * height * 3)
        var idx = 0
        for (y in 0 until height) {
            for (x in 0 until width) {
                val color = resized[x, y]
                val r = ((color shr 16) and 0xFF) / 255.0f
                val g = ((color shr 8) and 0xFF) / 255.0f
                val b = (color and 0xFF) / 255.0f
                pixels[idx++] = r
                pixels[idx++] = g
                pixels[idx++] = b
            }
        }
        return ImageData(pixels, width, height, ImageData.ImageFormat.RGB)
    }
}