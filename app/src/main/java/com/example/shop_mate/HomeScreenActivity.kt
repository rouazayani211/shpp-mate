package com.example.shop_mate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class HomeScreenActivity : ComponentActivity() {

    // MutableState to store products, loading status, error message, and logged-in user
    private val produits = mutableStateOf<List<Produit>>(emptyList())
    private val isLoading = mutableStateOf(false)
    private val errorMessage = mutableStateOf("")
    private val loggedInUser = mutableStateOf<UserData?>(null) // Updated to use `UserData`

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                HomeScreen(
                    produits = produits.value,
                    isLoading = isLoading.value,
                    errorMessage = errorMessage.value,
                    loggedInUser = loggedInUser.value ?: UserData(
                        nom = "Unknown",
                        prenom = "User",
                        email = "unknown@example.com",
                        imageProfile = null
                    )
                )
            }
        }

        // Fetch the logged-in user and products when the activity is created
        fetchLoggedInUser()
        fetchProduits()
    }

    // Function to fetch logged-in user details using `auth/profile`
    private fun fetchLoggedInUser() {
        val sharedPreferences = getSharedPreferences("shop_mate_prefs", MODE_PRIVATE)
        val email = sharedPreferences.getString("user_email", null)

        if (email.isNullOrEmpty()) {
            errorMessage.value = "User email is not available."
            return
        }

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val userResponse: Response<UserData> =
                    RetrofitInstance.userApi.getUserInfo(email)

                if (userResponse.isSuccessful) {
                    loggedInUser.value = userResponse.body()
                } else {
                    errorMessage.value =
                        "Failed to fetch user: ${userResponse.code()} - ${userResponse.message()}"
                }
            } catch (e: Exception) {
                errorMessage.value = "Error fetching user: ${e.localizedMessage}"
            }
        }
    }

    // Function to fetch products from the backend
    private fun fetchProduits() {
        isLoading.value = true
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response: Response<List<Produit>> = RetrofitInstance.produitApi.getProduits()
                if (response.isSuccessful) {
                    produits.value = response.body() ?: emptyList()
                } else {
                    errorMessage.value = "Error: ${response.code()} - ${response.message()}"
                }
            } catch (e: Exception) {
                errorMessage.value = "Exception fetching products: ${e.localizedMessage}"
            } finally {
                isLoading.value = false
            }
        }
    }
}
