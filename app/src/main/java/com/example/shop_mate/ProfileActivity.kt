package com.example.shop_mate

import UserViewModel
import UserViewModelFactory
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ProfileActivity : ComponentActivity() {

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewModel
        val userRepository = UserRepository(RetrofitInstance.userApi)
        userViewModel = ViewModelProvider(this, UserViewModelFactory(userRepository))[UserViewModel::class.java]

        // Load user profile
        loadUserProfile()

        // Set ProfileScreen as content
        setContent {
            ProfileScreen(viewModel = userViewModel)
        }
    }

    private fun loadUserProfile() {
        val sharedPreferences = getSharedPreferences("shop_mate_prefs", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("user_email", null)

        if (email.isNullOrEmpty()) {
            Toast.makeText(this, "User email not found. Please log in again.", Toast.LENGTH_SHORT).show()
            logoutAndNavigateToLogin()
            return
        }

        // Fetch user info using ViewModel's coroutine scope
        userViewModel.viewModelScope.launch {
            try {
                val response = userViewModel.fetchUserDataFromApi(email)
                if (response != null) {
                    Log.d("ProfileActivity", "User data loaded: $response")
                } else {
                    Log.e("ProfileActivity", "Failed to load user data")
                    showToastAndLogout()
                }
            } catch (e: Exception) {
                Log.e("ProfileActivity", "Error fetching user data: ${e.message}")
                showToastAndLogout()
            }
        }
    }

    private fun showToastAndLogout() {
        runOnUiThread {
            Toast.makeText(this@ProfileActivity, "Error fetching user data. Please log in again.", Toast.LENGTH_SHORT).show()
            logoutAndNavigateToLogin()
        }
    }

    private fun logoutAndNavigateToLogin() {
        val sharedPreferences = getSharedPreferences("shop_mate_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
