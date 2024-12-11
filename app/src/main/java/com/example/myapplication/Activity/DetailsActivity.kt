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

        // Retrieve product details
        val productName = intent.getStringExtra("PRODUCT_NAME") ?: run {
            showToast("Product name is missing.")
            "Unknown Product"
        }
        val productPrice = intent.getDoubleExtra("PRODUCT_PRICE", -1.0).takeIf { it >= 0 } ?: run {
            showToast("Product price is missing or invalid.")
            0.0
        }
        val productImage = intent.getStringExtra("PRODUCT_IMAGE") ?: run {
            showToast("Product image is missing.")
            ""
        }

        // Retrieve UserData
        val userData = intent.getParcelableExtra<UserData>("USER_DATA") ?: run {
            showToast("User data is missing.")
            UserData(
                id = "",
                nom = "Unknown",
                prenom = "User",
                email = "",
                password = "",
                imageProfile = ""
            )
        }

        // Extract profile image from UserData
        val profileImage = userData.imageProfile

        // Log the retrieved data for debugging
        Log.d("DetailsActivity", "Product Name: $productName")
        Log.d("DetailsActivity", "Product Price: $productPrice")
        Log.d("DetailsActivity", "Product Image: $productImage")
        Log.d("DetailsActivity", "Profile Image: $profileImage")

        setContent {
            DetailsScreen(
                name = productName,
                price = productPrice,
                profileUrl = if (profileImage.isNotBlank()) "http://192.168.223.172:3000/uploads/$profileImage" else "",
                imageUrl = if (productImage.isNotBlank()) "http://192.168.223.172:3000/uploads/$productImage" else ""
            )
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
