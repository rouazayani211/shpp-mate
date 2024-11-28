package com.example.shop_mate

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Response

class ProduitViewModel : ViewModel() {

    // State variables for managing the product list, loading state, and error messages
    var produits = mutableStateOf<List<Produit>>(emptyList())
    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf("")

    // Fetch products from the backend
    fun fetchProduits() {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response: Response<List<Produit>> = RetrofitInstance.produitApi.getProduits()
                if (response.isSuccessful) {
                    produits.value = response.body() ?: emptyList()
                } else {
                    errorMessage.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage.value = "Exception: ${e.localizedMessage}"
            } finally {
                isLoading.value = false
            }
        }
    }
}
