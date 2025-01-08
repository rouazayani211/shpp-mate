package com.example.myapplication



import com.example.myapplication.Model.AIRequest
import com.example.myapplication.Model.AIResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface GeminiService {
    @POST("v1beta/models/gemini-1.5-flash:generateContent")
    suspend fun generateContent(@Body request: AIRequest): AIResponse
}






