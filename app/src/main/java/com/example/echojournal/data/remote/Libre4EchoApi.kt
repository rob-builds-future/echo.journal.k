package com.example.echojournal.data.remote

import com.example.echojournal.data.remote.model.TranslateRequest
import com.example.echojournal.data.remote.model.TranslateResponse
import retrofit2.http.Body
import retrofit2.http.POST


const val BASE_URL = "https://libre4echo.de/"

interface LibreTranslateService {
    @POST("translate")
    suspend fun translate(
        @Body request: TranslateRequest
    ): TranslateResponse
}