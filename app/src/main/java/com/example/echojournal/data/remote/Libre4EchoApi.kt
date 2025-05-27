package com.example.echojournal.data.remote

import com.example.echojournal.data.remote.model.TranslateRequest
import com.example.echojournal.data.remote.model.TranslateResponse
import com.example.echojournal.data.remote.model.util.LanguageDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


const val BASE_URL = "https://libre4echo.de/"

interface LibreTranslateService {
    @POST("translate")
    suspend fun translate(@Body request: TranslateRequest): TranslateResponse

    @GET("languages")
    suspend fun getLanguages(): List<LanguageDto>
}