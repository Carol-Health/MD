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
        val descriptionTextView: TextView = findViewById(R.id.descriptionTextView)
        val treatmentTextView: TextView = findViewById(R.id.treatmentTextView)
        val resultImageView: ImageView = findViewById(R.id.resultImageView)

        val diseaseName = intent.getStringExtra("diseaseName") ?: "Unknown"
        val description = intent.getStringExtra("description") ?: "No description available."
        val treatment = intent.getStringExtra("treatment") ?: "No treatment information available."
        val imageUriString = intent.getStringExtra("imageUri")

        diseaseNameTextView.text = diseaseName
        descriptionTextView.text = description
        treatmentTextView.text = treatment

        if (!imageUriString.isNullOrEmpty()) {
            val imageUri = Uri.parse(imageUriString)
            resultImageView.setImageURI(imageUri)
        }
    }
}
