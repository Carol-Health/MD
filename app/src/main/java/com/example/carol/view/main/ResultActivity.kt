package com.example.carol.view.main

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.widget.ImageView
import android.widget.TextView
import com.example.carol.R

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val toolbar: Toolbar = findViewById(R.id.topBar)
        toolbar.setNavigationOnClickListener { finish() }

        val diseaseNameTextView: TextView = findViewById(R.id.diseaseNameTextView)
        val confidenceScoreTextView: TextView = findViewById(R.id.confidenceScoreTextView)
        val resultImageView: ImageView = findViewById(R.id.resultImageView)

        val diseaseName = intent.getStringExtra("diseaseName") ?: "Unknown"
        val confidenceScore = intent.getFloatExtra("confidenceScore", 0f)
        val imageUriString = intent.getStringExtra("imageUri")

        diseaseNameTextView.text = "Disease: $diseaseName"
        confidenceScoreTextView.text = "Confidence: ${(confidenceScore * 100).toInt()}%"

        if (!imageUriString.isNullOrEmpty()) {
            val imageUri = Uri.parse(imageUriString)
            resultImageView.setImageURI(imageUri)
        }
    }
}