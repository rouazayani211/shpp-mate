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

        setContent {
            CartScreen(
                cartItems = CartManager.cartItems,
                navigateToConfirmation = { navigateToConfirmationScreen() } // Handle navigation
            )
        }
    }

    private fun navigateToConfirmationScreen() {
//        val intent = Intent(this, ConfirmationActivity::class.java)
//        startActivity(intent) // Navigate to the confirmation screen
    }
}
