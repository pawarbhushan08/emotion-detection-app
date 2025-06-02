package com.bhushan.android.emotiondetectionapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.bhushan.android.emotiondetectionapp.ui.theme.EmotionDetectionAppTheme
import com.bhushan.android.presentation.camera.ui.CameraViewScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EmotionDetectionAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CameraViewScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}