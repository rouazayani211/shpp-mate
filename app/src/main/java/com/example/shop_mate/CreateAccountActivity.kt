package com.example.shop_mate

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.IOException

class CreateAccountActivity : ComponentActivity() {

    private val TAG = "CreateAccountActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                CreateAccountScreen(
                    onSignInClick = { nom, prenom, email, password, imageProfileUri ->
                        createAccount(nom, prenom, email, password, imageProfileUri)
                    },
                    onNavigateToLogin = {
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                )
            }
        }
    }

    private fun createAccount(
        nom: String,
        prenom: String,
        email: String,
        password: String,
        imageProfileUri: Uri?
    ) {
        lifecycleScope.launch {
            try {
                val api = RetrofitInstance.userApi

                // Prepare the image for upload
                val imagePart = if (imageProfileUri != null) {
                    val fileName = imageProfileUri.lastPathSegment ?: "profile_image.jpg"
                    val inputStream = contentResolver.openInputStream(imageProfileUri)
                    val imageBytes = inputStream?.readBytes()
                    val requestBody = imageBytes?.toRequestBody("image/*".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("file", fileName, requestBody!!)
                } else null

                // Call the API with the multipart data
                val response = if (imagePart != null) {
                    api.createAccountWithImage(
                        nom.toRequestBody("text/plain".toMediaTypeOrNull()),
                        prenom.toRequestBody("text/plain".toMediaTypeOrNull()),
                        email.toRequestBody("text/plain".toMediaTypeOrNull()),
                        password.toRequestBody("text/plain".toMediaTypeOrNull()),
                        imagePart
                    )
                } else {
                    api.createAccount(
                        CreateUserRequest(
                            nom = nom,
                            prenom = prenom,
                            email = email,
                            password = password,
                            imageProfile = null // No image
                        )
                    )
                }

                if (response.isSuccessful) {
                    Toast.makeText(this@CreateAccountActivity, "Account created successfully! Please verify your email.", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@CreateAccountActivity, LoginActivity::class.java))
                    finish()
                } else {
                    val error = response.errorBody()?.string() ?: "Failed to create account"
                    Toast.makeText(this@CreateAccountActivity, error, Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                Toast.makeText(this@CreateAccountActivity, "Network error: ${e.message}", Toast.LENGTH_SHORT).show()
            } catch (e: HttpException) {
                Toast.makeText(this@CreateAccountActivity, "Server error: ${e.message}", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@CreateAccountActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }


}
