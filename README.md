# Emotion Detection App

This project provides a real-time **Emotion Detection** feature using your device's camera. It leverages both **TensorFlow Lite (TFLite)** and **ONNX** machine learning models to analyze facial expressions and classify emotions directly on your Android device.

## Features

- 📷 **Live Camera Preview:** View yourself in real-time as emotion detection happens.
- 🧠 **Dual Model Support:** Analyze emotions with either the TFLite model, the ONNX model.
- ⚡ **Real-Time Inference:** Detects emotions instantly as you move or change your facial expression.
- 🎛️ **Model Selection:** Easily switch between TFLite, ONNX, or Both using a simple radio button interface in the UI.
- 📝 **Emotion Results:** Displays emotion predictions for each model side-by-side.

## Supported Emotions

The models can recognize the following emotions:

- Angry
- Disgust
- Fear
- Happy
- Sad
- Surprise
- Neutral

## How It Works

1. **Camera Feed**  
   The app uses CameraX to provide a live camera feed.

2. **Image Preprocessing**  
   - For **TFLite**, the input is converted to a 48x48 grayscale image.
   - For **ONNX**, the input is converted to a 224x224 RGB image with proper normalization.

3. **Model Inference**  
   - The preprocessed image is fed into the selected model(s) (TFLite/ONNX).
   - The model outputs a probability distribution over the emotion classes.

4. **Result Display**  
   - The predicted emotion(s) are shown on the UI in real time.

## UI Example

- **Camera Preview:** Takes up most of the screen.
- **Model Selector:** Radio buttons labeled "TFLite", "ONNX", "Both" above or below the preview.
- **Emotion Results:** Shows the prediction from each selected model.

## Code Highlights

- Uses `ImageAnalysis.Analyzer` for real-time frame analysis.
- Allows dynamic switching between models without restarting the camera.
- Efficient threading for smooth camera and inference performance.

## Example Usage

1. **Grant Camera Permission** when prompted.
2. **Select Model**: Choose between TFLite, ONNX, or Both.
3. **View Results**: See your current emotion detected live at the bottom of the screen.

## Requirements

- Android device with a camera.
- Minimum SDK: 21+
- Camera permission granted.

## Customization

- Swap in your own emotion detection models (TFLite or ONNX formats).
- Extend the UI for more advanced feedback or analytics.

## License

This project is for research and educational purposes. Check the LICENSE file for details.

