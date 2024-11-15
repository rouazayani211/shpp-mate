package com.example.shop_mate

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    // Login endpoint
    @POST("auth/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    // Signup endpoint
    @POST("auth/signup")
    suspend fun createAccount(@Body createUserRequest: CreateUserRequest): Response<CreateUserResponse>
}
