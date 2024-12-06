package com.example.myapplication.Model

import android.os.Parcel
import android.os.Parcelable
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
    val message: String? // Additional message (e.g., errors or status messages)
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
data class UpdateUserRequest(
    val nom: String,
    val prenom: String,
    val email: String,
    val password: String?,
    val imageProfile: String? = null
)

