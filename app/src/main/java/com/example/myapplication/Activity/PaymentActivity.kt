package com.example.myapplication.Activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.Model.CartItem
import com.example.myapplication.Repository.UserRepository
import com.example.myapplication.Screen.PaymentScreen
import com.example.myapplication.UserApi
import com.example.myapplication.ViewModel.CartViewModel
import com.example.myapplication.ViewModel.UserViewModel
import com.example.myapplication.ViewModel.UserViewModelFactory
import com.example.myapplication.Network.RetrofitInstance

class PaymentActivity : ComponentActivity() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var cartViewModel: CartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize UserRepository and UserViewModel
        val userApi = RetrofitInstance.userApi // Use the pre-configured Retrofit instance
        val userRepository = UserRepository(userApi)
        val userViewModelFactory = UserViewModelFactory(userRepository)
        userViewModel = ViewModelProvider(this, userViewModelFactory)[UserViewModel::class.java]

        // Initialize CartViewModel
        cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]

        // Fetch the user email from SharedPreferences
        val sharedPreferences = getSharedPreferences("shop_mate_prefs", MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("user_email", null)

        // Fetch user data using the email
        if (userEmail.isNullOrEmpty()) {
            // Handle the case where the email is not available
            finish() // Close the activity if user email is not found
            return
        } else {
            userViewModel.fetchUserDataFromApi(userEmail)
        }

        // Retrieve CartItems from Intent
        val cartItems = intent.getSerializableExtra("cartItems") as? ArrayList<CartItem> ?: arrayListOf()

        // Set content with PaymentScreen
        setContent {
            PaymentScreen(
                userViewModel = userViewModel,
                cartViewModel = cartViewModel, // Pass the cartViewModel
                cartItems = cartItems,
                onPaymentSuccess = {
                    finish() // Handle payment success
                }
            )
        }
    }
}
