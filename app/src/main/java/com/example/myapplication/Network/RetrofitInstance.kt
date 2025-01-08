package com.example.myapplication.Network

import com.example.myapplication.GeminiService
import com.example.myapplication.PaymentApi
import com.example.myapplication.ProduitApiService
import com.example.myapplication.ScraperApiService
import com.example.myapplication.UserApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "http://192.168.48.172:3000/" // Localhost for your server
    private const val GEMINI_BASE_URL = "https://generativelanguage.googleapis.com/" // Gemini API base URL
    private const val GEMINI_API_KEY = "AIzaSyCmgn8UEsl_zLRnJYn7lQgyxgBx3Iv6Xj8" // Replace with your actual key

    private val loggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val geminiApiKeyInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val urlWithKey = originalRequest.url.newBuilder()
            .addQueryParameter("key", GEMINI_API_KEY) // Add the API key as a query parameter
            .build()
        val newRequest = originalRequest.newBuilder()
            .url(urlWithKey)
            .build()
        chain.proceed(newRequest)
    }

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(geminiApiKeyInterceptor) // Add the API key interceptor
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Generic function to create API service instances dynamically
    fun <T> createService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }

    // User API service
    val userApi: UserApi by lazy {
        createService(UserApi::class.java)
    }

    // Product API service
    val produitApi: ProduitApiService by lazy {
        createService(ProduitApiService::class.java)
    }

    val api: ScraperApiService by lazy {
        retrofit.create(ScraperApiService::class.java)
    }

    val paymentApi: PaymentApi by lazy {
        createService(PaymentApi::class.java)
    }

    private val geminiRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl(GEMINI_BASE_URL)
            .client(okHttpClient) // Use the same client with the API key interceptor
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Gemini API Service
    val geminiApi: GeminiService by lazy {
        geminiRetrofit.create(GeminiService::class.java)
    }
}
