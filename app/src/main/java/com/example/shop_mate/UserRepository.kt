package com.example.shop_mate

import android.util.Log
import retrofit2.Response

class UserRepository(private val api: UserApi) {
    suspend fun loginUser(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = api.loginUser(LoginRequest(email, password))
            Log.d("LoginResponse", "Raw Response: ${response.body()?.toString()}") // Debug API response
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Error: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Log.e("LoginResponse", "Exception: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun getUserInfo(email: String): Response<UserData> {
        return api.getUserInfo(email)
    }
}


/**
     * Fetches the user details using a JWT token.
     * Returns the [UserData] object on success or throws an [Exception] on failure.
     */
   /* suspend fun getUserDetails(token: String): UserData {
        return try {
            val response = api.getUserDetails("Bearer $token")
            if (response.isSuccessful) {
                response.body() ?: throw Exception("Empty user details response")
            } else {
                throw Exception("Failed to fetch user details: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            throw Exception("Error fetching user details: ${e.message}")
        }
    }*/
