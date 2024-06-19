package com.bangkit.capstone.gestura.view.detectmovement

import com.bangkit.capstone.gestura.ml.ModelVGG
import android.content.Context
import android.graphics.Bitmap
import android.os.SystemClock
import android.util.Log
import android.view.Surface
import androidx.camera.core.ImageProxy
import com.bangkit.capstone.gestura.R
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.image.ops.Rot90Op
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions
import java.nio.ByteBuffer
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

class ObjectDetectionHelpe(
    val context: Context,
    val detectorListener: DetectorListener?,
    val modelName: String = "modelVGG.tflite",
    var threshold: Float = 0.1f,
    var maxResults: Int = 3,

) {
    private var modelVGG: ModelVGG? = null
    private var imageClassifier: ImageClassifier? = null

    init {
        setupModel()
//        setupImageClassifier()
    }

    private fun setupModel() {
        try {
            modelVGG = ModelVGG.newInstance(context)
        } catch (e: Exception) {
            detectorListener?.onError(context.getString(R.string.model_initialization_failed))
            Log.d(TAG, e.message.toString())
        }
    }

    fun detectObject(image: ImageProxy) {
        if (modelVGG == null) {
            setupModel()
        }

        // Convert ImageProxy to Bitmap
        val bitmap = toBitmap(image)

        // Upscale the resized bitmap to 128x128
        val upscaledBitmap = Bitmap.createScaledBitmap(bitmap, 256, 256, true)

        // Ensure the image is resized and rotated correctly
        val imageProcessor = ImageProcessor.Builder()
            .add(Rot90Op(-image.imageInfo.rotationDegrees / 90))
            .build()

        // Process the upscaled image to a TensorImage
        val tensorImage = imageProcessor.process(TensorImage.fromBitmap(upscaledBitmap))

        val imageProcessingOptions = ImageProcessingOptions.builder()
            .setOrientation(getOrientationFromRotation(image.imageInfo.rotationDegrees))
            .build()

        val byteBuffer: ByteBuffer = tensorImage.buffer

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 128, 128, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(byteBuffer)

        var inferenceTime = SystemClock.uptimeMillis()



//        val results = imageClassifier?.classify(tensorImage, imageProcessingOptions)
//        inferenceTime = SystemClock.uptimeMillis() - inferenceTime

        val outputs = modelVGG?.process(inputFeature0)
        val outputFeature0 = outputs?.outputFeature0AsTensorBuffer

//        Log.d("Moza Adirafi", outputs.toString())
//
//        Log.d("Rafli Wasis", outputFeature0.toString())

//         Handle the output as needed
        val results = handleOutput(outputFeature0)

        Log.d("Shafa Najwa", results.toString())


//        detectorListener?.onResults(
//            results,
//            inferenceTime,
//            tensorImage.height,
//            tensorImage.width
//        )

        detectorListener?.onResults(
            results,
            inferenceTime,
            tensorImage.height,
            tensorImage.width
        )
    }

    private fun toBitmap(image: ImageProxy): Bitmap {
        val bitmapBuffer = Bitmap.createBitmap(
            image.width,
            image.height,
            Bitmap.Config.ARGB_8888
        )
        image.use { bitmapBuffer.copyPixelsFromBuffer(image.planes[0].buffer) }
        image.close()
        return bitmapBuffer
    }

    private fun handleOutput(outputFeature0: TensorBuffer?): MutableList<String> {
        val labels = ('A'..'Z').toList()
        val probabilities = outputFeature0?.floatArray ?: return mutableListOf()

        val probabilitiesList = outputFeature0?.floatArray

        Log.d("Fatrin", probabilities.toString())
        Log.d("Zidan", probabilitiesList.toString())

        val maxIndex = probabilities.indices.maxByOrNull { probabilities[it] } ?: -1
        if (maxIndex == -1) return mutableListOf()

        val maxLabel = labels[maxIndex]
        val maxProbability = probabilities[maxIndex] * 100

        return mutableListOf("$maxLabel: %.2f%%".format(maxProbability))
    }

    companion object {
        private const val TAG = "ObjectDetectionHelper"
    }

    private fun getOrientationFromRotation(rotation: Int): ImageProcessingOptions.Orientation {
        return when (rotation) {
            Surface.ROTATION_270 -> ImageProcessingOptions.Orientation.BOTTOM_RIGHT
            Surface.ROTATION_180 -> ImageProcessingOptions.Orientation.RIGHT_BOTTOM
            Surface.ROTATION_90 -> ImageProcessingOptions.Orientation.TOP_LEFT
            else -> ImageProcessingOptions.Orientation.RIGHT_TOP
        }
    }

    interface DetectorListener {
        fun onError(error: String)
        fun onResults(
            results: MutableList<String>?,
            inferenceTime: Long,
            imageHeight: Int,
            imageWidth: Int
        )
    }

    interface ClassifierListener {
        fun onError(error: String)
        fun onResults(
            results: List<Classifications>?,
            inferenceTime: Long
        )
    }
}
