package com.example.myapplication.Model

import androidx.compose.ui.graphics.Color // USE THIS
import android.os.Parcelable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.parcelize.Parcelize
import java.io.Serializable


// Data model for login request
data class LoginRequest(
    val email: String,
    val password: String
)

// Data model for login response
data class LoginResponse(
    val success: Boolean,
    val token: String?, // Token received upon successful login
    val isEmailVerified: Boolean, // Indicates if the user's email is verified
    val message: String? ,// Additional message (e.g., errors or status messages)
    val user: UserData? // Should match the API response for the logged-in user data

)
@Parcelize
data class UserData(
    val id:String,
    val nom: String,
    val prenom: String,
    val email: String,
    val password: String,
    val imageProfile: String // Assuming this is the profile image
) : Parcelable





// Data model for creating a new user
data class CreateUserRequest(
    val nom: String, // User's last name
    val prenom: String, // User's first name
    val email: String, // User's email address
    val password:String,
    val imageProfile: String?// User's password
)

// Response for creating a new user
data class CreateUserResponse(
    val success: Boolean, // Indicates if the user creation was successful
    val message: String // Additional message (e.g., success or error details)
)

// Request for password reset (forgot password)
data class ForgotPasswordRequest(
    val email: String // Email to send the reset code to
)

data class ResetPasswordRequest(
    val resetToken: String,
    val newPassword: String
)

data class Brand(
    val name: String,       // Name of the brand
    val latitude: Double,   // Latitude of the brand location
    val longitude: Double   // Longitude of the brand location
)
data class CartItem(
    val product: Produit,
    val quantity: Int,
    val size: String,
    val color: Int // Change type to store colors if necessary
) : Serializable

data class ComparisonResult(
    val productName: String,
    val userProductPrice: String,
    val zaraProductPrice: String,
    val userProductImage: String,
    val zaraProductImage: String,
    val comparisonResult: String
)
data class PriceComparison(
    val productName: String,
    val zaraProductPrice: String,
    val competitorPrice: String,
    val competitorName: String,
    val userProductPrice: String,
    val userProductImage: String,
    val zaraProductImage: String
)
data class UpdateUserRequest(
    val nom: String,
    val prenom: String,
    val email: String,
    val password: String?,
    val imageProfile: String?
)





data class AIRequest(
    val messages: List<Message>
)

data class Message(
    val content: String,
    val role: String
)

data class AIResponse(
    val candidates: List<Candidate>?
)

data class Candidate(
    val content: Content?
)

data class Content(
    val parts: List<Part>?
)

data class Part(
    val text: String?
)

