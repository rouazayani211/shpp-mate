package com.example.myapplication.Activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.myapplication.Network.CartManager
import com.example.myapplication.Screen.CartScreen

class CartActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the content view with the CartScreen
        setContent {
            CartScreen(
                cartItems = CartManager.cartItems, // Pass cart items from CartManager
                navigateToConfirmation = { navigateToPaymentScreen() } // Handle navigation
            )
        }
    }

    /**
     * Navigate to the payment screen.
     */
    private fun navigateToPaymentScreen() {
        // Navigate to PaymentActivity or any other relevant screen.
        val intent = Intent(this, PaymentActivity::class.java)
        startActivity(intent)
    }
}
