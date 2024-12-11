package com.example.carol.network

import com.google.gson.annotations.SerializedName

data class HistoryApiResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("data")
    val data: List<HistoryResponse>
)
