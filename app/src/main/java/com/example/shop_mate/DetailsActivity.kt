package com.example.shop_mate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class DetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve product details from the intent
        val productName = intent.getStringExtra("PRODUCT_NAME") ?: "Unknown"
        val productPrice = intent.getDoubleExtra("PRODUCT_PRICE", 0.0)
        val productImage = intent.getStringExtra("PRODUCT_IMAGE") ?: ""

        setContent {
            DetailsScreen(
                name = productName,
                price = productPrice,
                imageUrl = "http://192.168.79.172:3000/uploads/$productImage"
            )
        }
    }
}
