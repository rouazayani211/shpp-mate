package com.example.myapplication

import com.example.myapplication.Model.Brand
import com.example.myapplication.Model.ComparisonResult
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Path

interface ScraperApiService {
        @GET("scraper")
        fun scrapeClothingStore(
            @Query("lat") lat: Double,
            @Query("lng") lng: Double
        ): Call<List<Brand>>
        abstract fun compareProduct(@Path("id") id: String): Response<ComparisonResult>
    }



