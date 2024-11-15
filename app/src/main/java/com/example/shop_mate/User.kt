package com.example.shop_mate


data class LoginRequest(
    val email: String,
    val password: String
)

// Define the data model for login response
data class LoginResponse(
    val success: Boolean,
    val token: String?,
    val message: String?
)
data class CreateUserRequest(
    val nom: String,
    val prenom: String,
    val email: String,
    val password: String
)
data class CreateUserResponse(
    val success: Boolean,
    val message: String
)
