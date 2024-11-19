package com.example.shop_mate

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ForgotPasswordActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                ForgotPasswordScreen { email ->
                    sendResetCode(email)
                }
            }
        }
    }

    private fun sendResetCode(email: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.forgotPassword(
                    ForgotPasswordRequest(email)
                )
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@ForgotPasswordActivity,
                            "Reset code sent to your email!",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Navigate to VerificationActivity
                        val intent = Intent(this@ForgotPasswordActivity, VerificationActivity::class.java)
                        intent.putExtra("email", email) // Pass the email
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@ForgotPasswordActivity,
                            "Failed to send reset code. Try again.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(
                        this@ForgotPasswordActivity,
                        "An error occurred: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
