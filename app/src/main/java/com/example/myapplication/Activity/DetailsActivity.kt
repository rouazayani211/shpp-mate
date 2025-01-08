package com.example.myapplication.Activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.myapplication.Model.UserData
import com.example.myapplication.Screen.DetailsScreen

class DetailsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve product details with fallback values
        val productName = intent.getStringExtra("PRODUCT_NAME") ?: "Unknown Product"
        val productPrice = intent.getDoubleExtra("PRODUCT_PRICE", -1.0).takeIf { it >= 0 } ?: 0.0
        val productImage = intent.getStringExtra("PRODUCT_IMAGE") ?: ""

        // Retrieve UserData from intent
        val userData = intent.getParcelableExtra<UserData>("USER_DATA")
            ?: UserData(
                id = "",
                nom = "Unknown",
                prenom = "User",
                email = "",
                password = "",
                imageProfile = ""
            )

        // Construct profile image URL
        val profileImageUrl = userData.imageProfile?.takeIf { it.isNotBlank() }
            ?.let { "http://192.168.48.172:3000/uploads/$it" }
            ?: ""

        // Construct product image URL
        val productImageUrl = productImage.takeIf { it.isNotBlank() }
            ?.let { "http://192.168.48.172:3000/uploads/$it" }
            ?: ""

        // Simple logging for debugging
        logDetails(productName, productPrice, productImageUrl, profileImageUrl)

        // Set Compose content with DetailsScreen
        setContent {
            DetailsScreen(
                name = productName,
                price = productPrice,
                imageUrl = productImageUrl,
                profileUrl = profileImageUrl
            )
        }
    }

    private fun logDetails(productName: String, productPrice: Double, productImageUrl: String, profileImageUrl: String) {
        Log.d("DetailsActivity", "Product Name: $productName")
        Log.d("DetailsActivity", "Product Price: $productPrice")
        Log.d("DetailsActivity", "Product Image: $productImageUrl")
        Log.d("DetailsActivity", "Profile Image: $profileImageUrl")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
