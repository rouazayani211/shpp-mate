package com.example.shop_mate

import UserViewModel
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme

class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize UserApi and UserRepository
        val userApi = RetrofitInstance.api
        val userRepository = UserRepository(userApi)

        // Create an instance of UserViewModel directly
        val userViewModel = UserViewModel(userRepository)

        // Display the LoginScreen
        setContent {
            MaterialTheme {
                LoginScreen(
                    viewModel = userViewModel,
                    onLogin = { email, password ->
                        userViewModel.loginUser(email, password) { result ->
                            result.onSuccess {
                                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                                // Navigate to the next screen, for example:
                                // startActivity(Intent(this, MainActivity::class.java))
                                // finish()
                            }.onFailure { error ->
                                Toast.makeText(this, "Login failed: ${error.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    onCreateAccountClick = {
                        // Navigate to CreateAccountActivity
                        val intent = Intent(this, CreateAccountActivity::class.java)
                        startActivity(intent)
                    }
                )
            }
        }
    }
}
