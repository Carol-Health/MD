package com.example.carol.network

import com.google.gson.annotations.SerializedName

data class DetectionResponse(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("treatment")
	val treatment: String? = null,

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("confidence")
	val confidence: Float? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null
)
