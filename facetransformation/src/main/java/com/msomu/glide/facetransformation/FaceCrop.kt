package com.msomu.glide.facetransformation

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PointF
import android.graphics.RectF
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest
import kotlin.math.max

class FaceCrop : BitmapTransformation() {
    companion object {
        private const val VERSION = 1
        private const val ID =
            "com.msomu.glide.facetransformation.$VERSION"
        private val ID_BYTES =
            ID.toByteArray(Key.CHARSET)
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
    }

    override fun transform(
        bitmapPool: BitmapPool, original: Bitmap,
        width: Int, height: Int
    ): Bitmap {
        val scaleX = width.toFloat() / original.width
        val scaleY = height.toFloat() / original.height
        if (scaleX != scaleY) {
            val config = original.config ?: Bitmap.Config.ARGB_8888
            val result: Bitmap = bitmapPool.get(width, height, config)
            val scale = max(scaleX, scaleY)
            var left = 0f
            var top = 0f
            var scaledWidth = width.toFloat()
            var scaledHeight = height.toFloat()
            val focusPoint = detectFace(original)
            if (scaleX < scaleY) {
                scaledWidth = scale * original.width
                val faceCenterX = scale * focusPoint.x
                left = getLeftPoint(width, scaledWidth, faceCenterX)
            } else {
                scaledHeight = scale * original.height
                val faceCenterY = scale * focusPoint.y
                top = getTopPoint(height, scaledHeight, faceCenterY)
            }
            val targetRect =
                RectF(left, top, left + scaledWidth, top + scaledHeight)
            val canvas = Canvas(result)
            canvas.drawBitmap(original, null, targetRect, null)
            return result
        } else {
            return original
        }
    }

    private fun detectFace(original: Bitmap): PointF {
        val image = InputImage.fromBitmap(original, 0)
        val detector = FaceDetection.getClient()
        val faces = runBlocking { detector.process(image).await() }
        if (faces == null || faces.isEmpty()) {
            val centerPoint = PointF()
            centerPoint.set(
                (original.width / 2).toFloat(),
                (original.height / 2).toFloat()
            ) // center crop
            return centerPoint
        }
        val totalFaces = faces.size
        var sumX = 0f
        var sumY = 0f
        for (i in 0 until totalFaces) {
            val faceCenter = PointF()
            faceCenter.set(faces[i].boundingBox.exactCenterX(), faces[i].boundingBox.exactCenterY())
            sumX += faceCenter.x
            sumY += faceCenter.y
        }
        val focusPoint = PointF()
        focusPoint.set(sumX / totalFaces, sumY / totalFaces)
        return focusPoint
    }

    private fun getLeftPoint(width: Int, scaledWidth: Float, faceCenterX: Float) = when {
        faceCenterX <= width / 2 -> 0f // face is near the left edge.
        scaledWidth - faceCenterX <= width / 2 -> width - scaledWidth // face is near right edge
        else -> width / 2 - faceCenterX
    }

    private fun getTopPoint(height: Int, scaledHeight: Float, faceCenterY: Float) = when {
        faceCenterY <= height / 2 -> 0f // Face is near the top edge
        scaledHeight - faceCenterY <= height / 2 -> height - scaledHeight // face is near bottom edge
        else -> height / 2 - faceCenterY
    }
}