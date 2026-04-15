package com.example.lab7_photo_gallery_app

import android.graphics.Matrix
import android.os.Bundle
import android.view.ScaleGestureDetector
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class FullscreenActivity : AppCompatActivity() {

    private lateinit var matrix: Matrix
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var scale = 1f
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen)

        val imageResId = intent.getIntExtra("image_res_id", 0)
        imageView = findViewById(R.id.ivFullscreen)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)

        imageView.setImageResource(imageResId)
        matrix = Matrix()
        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())

        // Set the touch listener to handle pinch gestures
        imageView.setOnTouchListener { _, event ->
            scaleGestureDetector.onTouchEvent(event)
            true
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scale *= detector.scaleFactor
            // Limit zoom scale between 1x and 5x
            scale = Math.max(1.0f, Math.min(scale, 5.0f))

            matrix.setScale(scale, scale, detector.focusX, detector.focusY)
            imageView.imageMatrix = matrix
            return true
        }
    }
}
