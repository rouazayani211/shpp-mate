package com.example.shop_mate

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ResetPasswordActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val token = intent.getStringExtra("token") ?: ""

        setContent {
            MaterialTheme {
                ResetPasswordScreen(token) { newPassword ->
                    resetPassword(token, newPassword)
                }
            }
        }
    }

    private fun resetPassword(token: String, newPassword: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.resetPassword(
                    ResetPasswordRequest(token, newPassword)
                )
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@ResetPasswordActivity,
                            "Password reset successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                        navigateToLogin()
                    } else {
                        Toast.makeText(
                            this@ResetPasswordActivity,
                            "Failed to reset password. Please try again.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ResetPasswordActivity,
                        "An error occurred: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
