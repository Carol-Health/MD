package com.example.carol.network

import com.google.gson.annotations.SerializedName

data class DetectionResponse(
	val confidence: Float? = null,
	@SerializedName("class") val predictedClass: String? = null
)

