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

    // Forgot password endpoint
    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body forgotPasswordRequest: ForgotPasswordRequest): Response<Unit>
    @POST("auth/reset-password")
    suspend fun resetPassword(@Body resetPasswordRequest: ResetPasswordRequest): Response<Void>
    @POST("auth/verify-reset-code")
    suspend fun verifyResetCode(@Body request: VerifyResetCodeRequest): Response<VerifyResetCodeResponse>



}
