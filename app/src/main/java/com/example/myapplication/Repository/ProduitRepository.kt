package com.example.myapplication.Repository

import com.example.myapplication.Model.Produit
import com.example.myapplication.ProduitApiService

class ProduitRepository(private val api: ProduitApiService) {

    // Function to fetch all products
    suspend fun getAllProducts(): Result<List<Produit>> {
        return try {
            val response = api.getProduits()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Error: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Function to get a product by its ID
    suspend fun getProductById(id: String): Result<Produit> {
        return try {
            val response = api.getProduitById(id)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Error: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
