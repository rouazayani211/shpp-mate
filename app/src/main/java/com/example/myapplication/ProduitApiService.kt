package com.example.myapplication

import com.example.myapplication.Model.ComparisonResult
import com.example.myapplication.Model.Produit
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProduitApiService {


    @GET("produits")
    suspend fun getProduits(): Response<List<Produit>>
    @GET("produits/{id}")
    suspend fun getProduitById(@Path("id") id: String): Response<Produit>
    @GET("produits/compare/{id}")
    suspend fun compareProduct(@Path("id") id: String): Response<ComparisonResult>
}