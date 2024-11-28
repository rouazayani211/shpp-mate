package com.example.shop_mate

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object RetrofitInstance {
   // private const val BASE_URL = "http://10.0.0.2:3000" // Localhost for Android Emulator

   private const val BASE_URL = "http://192.168.79.172:3000" // Localhost for Android Emulator

    private val loggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
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
}
