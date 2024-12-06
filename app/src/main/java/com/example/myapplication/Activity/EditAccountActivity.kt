package com.example.myapplication.Activity

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.ViewModel.EditAccountViewModel
import com.example.myapplication.Screen.EditAccountScreen
import com.example.myapplication.Model.UserData

class EditAccountActivity : ComponentActivity() {

    private lateinit var editAccountViewModel: EditAccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the ViewModel
        editAccountViewModel = ViewModelProvider(this).get(EditAccountViewModel::class.java)

        // Retrieve the saved email from SharedPreferences
        val sharedPreferences = getSharedPreferences("shop_mate_prefs", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("user_email", "")

        if (email.isNullOrEmpty()) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            finish() // Exit the activity if the email is not found
            return
        }

        // Set content inside setContent block, which is required for Composable functions
        setContent {
            var userData by remember { mutableStateOf<UserData?>(null) }
            var isLoading by remember { mutableStateOf(true) }
            var errorMessage by remember { mutableStateOf("") }

            // Fetch user data
            editAccountViewModel.fetchUserData(email) { fetchedData ->
                if (fetchedData != null) {
                    userData = fetchedData
                } else {
                    errorMessage = "Failed to load user data"
                }
                isLoading = false
            }

            // Display loading indicator or the EditAccountScreen once the data is fetched
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                if (userData != null) {
                    // Pass user ID when saving changes
                    // Pass user ID when saving changes
                    EditAccountScreen(
                        userData = userData, // Pass the fetched user data
                        onSaveChanges = { nom, prenom, email, password, imageProfileUri ->
                            editAccountViewModel.updateUserData(
                                id = userData?.id ?: "", // Ensure the user ID is passed
                                nom = nom,
                                prenom = prenom,
                                email = email,
                                password = password,
                                imageProfileUri = imageProfileUri,
                                onSuccess = {
                                    Toast.makeText(this, "Account updated successfully!", Toast.LENGTH_SHORT).show()
                                    finish()
                                },
                                onError = { error ->
                                    Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    )
                } else {
                    // Handle error by displaying a Toast
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
