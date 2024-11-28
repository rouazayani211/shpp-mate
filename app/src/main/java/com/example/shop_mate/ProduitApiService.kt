package com.example.shop_mate
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ProduitApiService {


    @GET("produits")
    suspend fun getProduits(): Response<List<Produit>>
    @GET("produits/{id}")
    suspend fun getProduitById(@Path("id") id: String): Response<Produit>
}