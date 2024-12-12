package com.example.carol.network

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
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
):Parcelable
