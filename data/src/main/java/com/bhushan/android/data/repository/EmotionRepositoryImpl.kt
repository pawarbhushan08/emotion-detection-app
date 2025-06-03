package com.bhushan.android.data.repository

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtException
import ai.onnxruntime.OrtSession
import android.content.Context
import android.util.Log
import com.bhushan.android.data.utils.ONNXModelLoader
import com.bhushan.android.data.utils.TFLiteModelLoader
import com.bhushan.android.domain.model.EmotionResult
import com.bhushan.android.domain.model.ImageData
import com.bhushan.android.domain.repository.EmotionRepository
import org.tensorflow.lite.Interpreter

class EmotionRepositoryImpl(context: Context) : EmotionRepository {

    private val tfLiteOutputLabels =
        listOf("angry", "disgust", "fear", "happy", "sad", "surprise", "neutral")
    private val onnxOutputLabels =
        listOf("Angry", "Disgust", "Fear", "Happy", "Neutral", "Sad", "Surprise")

    //Q Why Lazy?
    private val ortEnv: OrtEnvironment by lazy { OrtEnvironment.getEnvironment() }
    private val ortSession: OrtSession by lazy {
        ortEnv.createSession(
            ONNXModelLoader.loadModelFile(context, "model.onnx")
        )
    }

    private val tfliteInterpreter: Interpreter by lazy {
        TFLiteModelLoader.loadModel(context, "emotion_model.tflite")
    }

    override suspend fun detectEmotionTFLite(imageData: ImageData): EmotionResult {
        try {
            val input = preprocessTFLiteInput(imageData)
            val output = Array(1) { FloatArray(tfLiteOutputLabels.size) }
            tfliteInterpreter.run(input, output)
            val maxIndex = output[0].indices.maxByOrNull { output[0][it] } ?: -1
            return EmotionResult(tfLiteOutputLabels[maxIndex])
        } catch (e: IllegalArgumentException) {
            Log.e("EmotionRepositoryImpl", "detectEmotionTFLite: ", e)
            return EmotionResult(emotion = "Error")
        }
    }

    override suspend fun detectEmotionOnnx(imageData: ImageData): EmotionResult {
        try {
            val input = preprocessOnnxInput(imageData.pixels)
            val tensor = OnnxTensor.createTensor(ortEnv, input)
            val output = ortSession.run(mapOf("pixel_values" to tensor))
            val outputTensor = output[0].value as Array<FloatArray>
            val maxIndex = outputTensor[0].indices.maxByOrNull { outputTensor[0][it] } ?: -1
            return EmotionResult(
                if (maxIndex >= 0) onnxOutputLabels[maxIndex] else "Unknown"
            )
        } catch (e: OrtException) {
            Log.e("EmotionRepositoryImpl", "detectEmotionOnnx: ", e)
            return EmotionResult(emotion = "Error")
        }
    }

    private fun preprocessTFLiteInput(image: ImageData): Array<Array<Array<FloatArray>>> {
        val size = 48
        val input = Array(1) { Array(size) { Array(size) { FloatArray(1) } } }
        for (y in 0 until size) {
            for (x in 0 until size) {
                input[0][y][x][0] = image.pixels[y * size + x]
            }
        }
        return input
    }

    private fun preprocessOnnxInput(
        pixels: FloatArray,
        width: Int = 224,
        height: Int = 224
    ): Array<Array<Array<FloatArray>>> {
        require(pixels.size == width * height * 3) {
            "Pixel array must be of size width*height*3 (${width * height * 3}), was ${pixels.size}"
        }
        val input = Array(1) { Array(3) { Array(height) { FloatArray(width) } } }
        var idx = 0
        for (y in 0 until height) {
            for (x in 0 until width) {
                input[0][0][y][x] = pixels[idx++] // R
                input[0][1][y][x] = pixels[idx++] // G
                input[0][2][y][x] = pixels[idx++] // B
            }
        }
        return input
    }
}