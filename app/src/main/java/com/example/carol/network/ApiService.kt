package com.example.carol.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @Multipart
    @POST("predict")
    fun detectDisease(
        @Part file: MultipartBody.Part,
        @Part("uid") uid: RequestBody
    ): Call<DetectionResponse>

    @GET("history/{uid}")
    fun getHistory(
        @Path("uid") uid: String): Call<HistoryApiResponse>
}