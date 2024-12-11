package com.example.carol.network

import com.google.gson.annotations.SerializedName

data class HistoryResponse(
    @field:SerializedName("createdAt")
    val date: String,

    @field:SerializedName("image_url")
    val imageUrl: String,

    @field:SerializedName("name")
    val diseaseName: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("treatment")
    val treatment: String
)
