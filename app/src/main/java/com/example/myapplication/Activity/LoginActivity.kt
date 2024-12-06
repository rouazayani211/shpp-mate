package com.example.myapplication.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.Network.RetrofitInstance
import com.example.myapplication.Repository.UserRepository
import com.example.myapplication.Screen.LoginScreen
import com.example.myapplication.ViewModel.UserViewModel
import com.example.myapplication.ViewModel.UserViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class LoginActivity : ComponentActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var userViewModel: UserViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private val RC_SIGN_IN = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("shop_mate_prefs", Context.MODE_PRIVATE)

        // Check if email exists in SharedPreferences
        val savedEmail = sharedPreferences.getString("user_email", null)
        if (savedEmail != null) {
            navigateToHomeScreen()
            return
        }

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Initialize ViewModel
        val userRepository = UserRepository(RetrofitInstance.userApi)
        userViewModel = ViewModelProvider(
            this,
            UserViewModelFactory(userRepository)
        )[UserViewModel::class.java]

        // Set LoginScreen as the content
        setContent {
            LoginScreen(
                viewModel = userViewModel,
                onLogin = { email, password ->
                    userViewModel.loginUser(email, password) { result ->
                        result.onSuccess {
                            saveEmail(email)
                            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                            navigateToHomeScreen()
                        }.onFailure { error ->
                            Toast.makeText(
                                this,
                                "Login failed: ${error.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                onCreateAccountClick = {
                    startActivity(Intent(this, CreateAccountActivity::class.java))
                },
                onForgotPasswordClick = {
                    startActivity(Intent(this, ForgotPasswordActivity::class.java))
                },
                onGoogleSignIn = {
                    signInWithGoogle()
                }
            )
        }
    }

    private fun saveEmail(email: String) {
        val editor = sharedPreferences.edit()
        editor.putString("user_email", email)
        editor.apply()
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                handleGoogleSignInResult(account)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google Sign-In failed: ${e.statusCode}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleGoogleSignInResult(account: GoogleSignInAccount?) {
        account?.let {
            val email = it.email ?: ""
            saveEmail(email) // Save the email after Google Sign-In
            Toast.makeText(this, "Welcome, ${it.displayName}!", Toast.LENGTH_SHORT).show()
            navigateToHomeScreen()
        }
    }

    private fun navigateToHomeScreen() {
        val intent = Intent(this, HomeScreenActivity::class.java)
        startActivity(intent)
        finish()
    }
}
