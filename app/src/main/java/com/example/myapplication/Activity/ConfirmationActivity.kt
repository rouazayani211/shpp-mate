package com.example.myapplication.Activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.myapplication.Network.CartManager
import com.example.myapplication.Network.RetrofitInstance
import com.example.myapplication.Screen.ConfirmationScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ConfirmationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ConfirmationScreen(
                onConfirm = { confirmOrder() },
                onCancel = { finish() } // Navigate back
            )
        }
    }

    private fun confirmOrder() {
        // Add logic to confirm the order and send it to the backend
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.userApi.addToPurchaseHistory(
                    userId = "USER_ID_HERE", // Replace with dynamic user ID
                    items = CartManager.getCartItemsNames()
                )
                if (response.isSuccessful) {
                    runOnUiThread {
                        Toast.makeText(this@ConfirmationActivity, "Order confirmed!", Toast.LENGTH_SHORT).show()
                        CartManager.clearCart() // Clear the cart
                        finish() // Close the confirmation screen
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@ConfirmationActivity, "Failed to confirm order", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@ConfirmationActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
