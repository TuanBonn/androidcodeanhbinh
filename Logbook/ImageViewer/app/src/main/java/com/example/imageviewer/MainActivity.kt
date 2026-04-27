package com.example.imageviewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var imgDisplay: ImageView
    private lateinit var btnPrevious: Button
    private lateinit var btnNext: Button
    private lateinit var tvImageInfo: TextView

    // Danh sách ảnh từ thư mục drawable
    private val images = listOf(R.drawable.pic1, R.drawable.pic2, R.drawable.pic3)
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ánh xạ View
        imgDisplay = findViewById(R.id.imgDisplay)
        btnPrevious = findViewById(R.id.btnPrevious)
        btnNext = findViewById(R.id.btnNext)
        tvImageInfo = findViewById(R.id.tvImageInfo)

        updateImage()

        btnNext.setOnClickListener {
            currentIndex = (currentIndex + 1) % images.size
            updateImage()
        }

        btnPrevious.setOnClickListener {
            currentIndex = if (currentIndex > 0) currentIndex - 1 else images.size - 1
            updateImage()
        }
    }

    private fun updateImage() {
        imgDisplay.setImageResource(images[currentIndex])
        tvImageInfo.text = "Hình ảnh ${currentIndex + 1} / ${images.size}"

        // Disable nút nếu ở đầu/cuối danh sách (tùy chọn)
        // btnPrevious.isEnabled = currentIndex > 0
        // btnNext.isEnabled = currentIndex < images.size - 1
    }
}