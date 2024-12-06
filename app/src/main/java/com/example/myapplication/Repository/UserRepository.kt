package com.example.myapplication.Repository

import android.util.Log
import com.example.myapplication.Model.LoginRequest
import com.example.myapplication.Model.LoginResponse
import com.example.myapplication.Model.UserData
import com.example.myapplication.UserApi
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
