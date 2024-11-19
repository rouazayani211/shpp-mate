package com.example.shop_mate

import retrofit2.Response

class UserRepository(private val api: UserApi) {
    suspend fun loginUser(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = api.loginUser(LoginRequest(email, password))
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Error: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
