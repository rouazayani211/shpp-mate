package com.example.shop_mate

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class CreateAccountActivity : ComponentActivity() {

    private val TAG = "CreateAccountActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                CreateAccountScreen(
                    onSignInClick = { nom, prenom, email, password ->
                        // Trigger createAccount when the "Sign Up" button is clicked
                        createAccount(nom, prenom, email, password)
                    },
                    onNavigateToLogin = {
                        // Navigate to LoginActivity
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                )
            }
        }

    }

    private fun createAccount(nom: String, prenom: String, email: String, password: String) {
        lifecycleScope.launch {
            val api = RetrofitInstance.api
            val createUserRequest = CreateUserRequest(nom, prenom, email, password)

            try {
                val response = api.createAccount(createUserRequest)

                // Log the HTTP status code and the raw response body
                Log.d(TAG, "createAccount: Status Code = ${response.code()}")
                Log.d(TAG, "createAccount: Response Body = ${response.body()}")

                if (response.isSuccessful && response.body()?.success == true) {
                    Log.d(TAG, "createAccount: Account created successfully")
                    Toast.makeText(this@CreateAccountActivity, "Account created successfully!", Toast.LENGTH_SHORT).show()
                    // Navigate to LoginActivity
                    val intent = Intent(this@CreateAccountActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Log the error response and display an error message
                    val error = response.body()?.message ?: "Failed to create account"
                    Log.e(TAG, "createAccount: Error Response Body = ${response.errorBody()?.string()}")
                    Toast.makeText(this@CreateAccountActivity, error, Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                Log.e(TAG, "createAccount: Network error - ${e.message}")
                Toast.makeText(this@CreateAccountActivity, "Network error: ${e.message}", Toast.LENGTH_SHORT).show()
            } catch (e: HttpException) {
                Log.e(TAG, "createAccount: HTTP error - ${e.message}")
                Log.e(TAG, "createAccount: HTTP error code - ${e.code()}")
                Toast.makeText(this@CreateAccountActivity, "Server error: ${e.message}", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e(TAG, "createAccount: Unexpected error - ${e.message}")
                Toast.makeText(this@CreateAccountActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
