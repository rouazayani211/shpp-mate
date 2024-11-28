package com.example.shop_mate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

class CartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        // Render the CartScreen with the items from CartManager
        setContent {
            CartScreen(cartItems = CartManager.cartItems)
        }
    }
}
