package com.example.myapplication.Activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.myapplication.Screen.DetailsScreen

class DetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve product details from the intent
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

        // Retrieve the profile image URL from the logged-in user
        val profileImage = intent.getStringExtra("PROFILE_IMAGE") ?: run{
            showToast("Profile image is missing.")
            ""
        }

        setContent {
            DetailsScreen(
                name = productName,
                price = productPrice,
                profileUrl ="http://192.168.223.172:3000/uploads/$profileImage",
                imageUrl = if (productImage.isNotBlank()) "http://192.168.223.172:3000/uploads/$productImage" else ""
            )
        }
    }

    /**
     * Displays a toast message to notify the user of any missing or invalid data.
     */
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
