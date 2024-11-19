package com.example.shop_mate

import UserViewModel
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

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
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }.onFailure { error ->
                                Toast.makeText(this, "Login failed: ${error.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    onCreateAccountClick = {
                        val intent = Intent(this, CreateAccountActivity::class.java)
                        startActivity(intent)
                    },
                    onForgotPasswordClick = {
                        val intent = Intent(this, ForgotPasswordActivity::class.java)
                        startActivity(intent)
                    },
                    onGoogleSignIn = {
                        signInWithGoogle()
                    }
                )
            }
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)
                handleSignInResult(account)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google Sign-In failed: ${e.statusCode}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleSignInResult(account: GoogleSignInAccount?) {
        account?.let {
            val email = it.email
            val displayName = it.displayName

            if (email != null && displayName != null) {
                // Save to database using Coroutine
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = RetrofitInstance.api.createAccount(
                            CreateUserRequest(
                                nom = displayName.split(" ").getOrNull(1) ?: "",
                                prenom = displayName.split(" ").getOrNull(0) ?: "",
                                email = email,
                                password = "" // Leave password empty for Google sign-ins
                            )
                        )
                        if (response.isSuccessful) {
                            // Navigate to MainActivity
                            runOnUiThread {
                                Toast.makeText(this@LoginActivity, "Welcome, $displayName!", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                intent.putExtra("email", email)
                                intent.putExtra("name", displayName)
                                startActivity(intent)
                                finish()
                            }
                        } else {
                            runOnUiThread {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Failed to save user. Try again.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } catch (e: Exception) {
                        runOnUiThread {
                            Toast.makeText(
                                this@LoginActivity,
                                "Error: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Failed to retrieve account details.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
