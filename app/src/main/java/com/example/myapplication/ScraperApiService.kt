package com.example.myapplication

import com.example.myapplication.Model.Brand
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call

    interface ScraperApiService {
        @GET("scraper")
        fun scrapeClothingStore(
            @Query("lat") lat: Double,
            @Query("lng") lng: Double
        ): Call<List<Brand>>
    }



