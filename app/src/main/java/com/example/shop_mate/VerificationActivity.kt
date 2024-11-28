package com.example.shop_mate

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VerificationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val email = intent.getStringExtra("email") ?: ""

        if (email.isBlank()) {
            showToast("Email is missing. Please try again.")
            finish()
            return
        }

        setContent {
            MaterialTheme {
                OTPVerificationScreen { resetToken, newPassword ->
                    if (resetToken.isBlank() || newPassword.isBlank()) {
                        showToast("Both reset token and new password are required.")
                    } else {
                        resetPassword(resetToken, newPassword)
                    }
                }
            }
        }
    }

    private fun resetPassword(resetToken: String, newPassword: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                Log.d("VerificationActivity", "Resetting password with token: $resetToken")

                val resetResponse = RetrofitInstance.userApi.resetPassword(
                    ResetPasswordRequest(resetToken = resetToken, newPassword = newPassword)
                )

                withContext(Dispatchers.Main) {
                    if (resetResponse.isSuccessful) {
                        showToast("Password reset successful!")
                        navigateToLogin()
                    } else {
                        val errorMessage = resetResponse.errorBody()?.string()
                        showToast("Password reset failed: $errorMessage")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("VerificationActivity", "Exception occurred: ${e.message}")
                    showToast("An error occurred: ${e.message}")
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
