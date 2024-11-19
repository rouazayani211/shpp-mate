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

        val email = intent.getStringExtra("email") ?: "" // Retrieve the email from ForgotPasswordActivity

        setContent {
            MaterialTheme {
                OTPVerificationScreen { verificationCode ->
                    if (verificationCode.isBlank()) {
                        Toast.makeText(
                            this,
                            "Please enter a valid verification code.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        verifyCode(email, verificationCode)
                    }
                }
            }
        }
    }

    private fun verifyCode(email: String, verificationCode: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.verifyResetCode(
                    VerifyResetCodeRequest(email, verificationCode)
                )
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val token = response.body()?.token // Extract the token
                        Log.d("VerificationActivity", "Token received: $token")

                        if (token != null) {
                            Toast.makeText(
                                this@VerificationActivity,
                                "Verification successful!",
                                Toast.LENGTH_SHORT
                            ).show()

                            // Navigate to ResetPasswordActivity
                            val intent = Intent(this@VerificationActivity, ResetPasswordActivity::class.java)
                            intent.putExtra("token", token) // Pass the token for reset
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                this@VerificationActivity,
                                "Token missing in the response. Try again.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@VerificationActivity,
                            "Verification failed. Please try again.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@VerificationActivity,
                        "An error occurred: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
