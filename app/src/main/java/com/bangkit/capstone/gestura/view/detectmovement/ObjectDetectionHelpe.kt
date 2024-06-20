package com.bangkit.capstone.gestura.view.detectmovement

import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF
import android.os.SystemClock
import android.util.Log
import android.view.Surface
import androidx.camera.core.ImageProxy
import com.bangkit.capstone.gestura.R
import com.bangkit.capstone.gestura.ml.ModelVGG16
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.image.ops.Rot90Op
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions

class ObjectDetectionHelpe(
    val context: Context,
    val detectorListener: DetectorListener?,
) {
    private var modelVGG: ModelVGG16? = null

    init {
        setupModel()
    }

    private fun setupModel() {
        try {
            modelVGG = ModelVGG16.newInstance(context)
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

        // Ensure the image is resized and rotated correctly
        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
            .add(CastOp(DataType.FLOAT32))
            .add(Rot90Op(-image.imageInfo.rotationDegrees / 90))
            .build()

        val tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmap))

        val imageProcessingOptions = ImageProcessingOptions.builder()
            .setOrientation(getOrientationFromRotation(image.imageInfo.rotationDegrees))
            .build()

        val tensorBuffer: TensorBuffer = tensorImage.tensorBuffer

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(tensorImage.buffer)

        var inferenceTime = SystemClock.uptimeMillis()
        val outputs = modelVGG?.process(inputFeature0)
        inferenceTime = SystemClock.uptimeMillis() - inferenceTime

        val outputFeature0 = outputs?.outputFeature0AsTensorBuffer

        val confidences = outputFeature0!!.floatArray
        var maxPos = 0
        var maxConfidence = 0f
        for (i in confidences.indices) {
            if (confidences[i] > maxConfidence) {
                maxConfidence = confidences[i]
                maxPos = i
            }
        }

        val results = handleOutput(outputFeature0)

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

    private fun handleOutput(outputFeature0: TensorBuffer?): MutableList<DetectionResult> {
        val labels = ('A'..'Z').toList()
        val probabilities = outputFeature0?.floatArray ?: return mutableListOf()

        val results = mutableListOf<DetectionResult>()
        for (i in probabilities.indices) {
            if (probabilities[i] > 0.5) {  // Consider only high-confidence detections
                val boundingBox = RectF(0.1f, 0.1f, 0.3f, 0.3f) // Example bounding box
                results.add(DetectionResult(boundingBox, labels[i].toString(), probabilities[i]))
            }
        }
        return results
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
            results: MutableList<DetectionResult>?,
            inferenceTime: Long,
            imageHeight: Int,
            imageWidth: Int
        )
    }
}
