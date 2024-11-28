package com.example.shop_mate

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {
    // Login endpoint
    @POST("auth/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    // Signup endpoint
    @Multipart
    @POST("auth/signup")
    suspend fun createAccountWithImage(
        @Part("nom") nom: RequestBody,
        @Part("prenom") prenom: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part file: MultipartBody.Part
    ): Response<CreateUserResponse>
    @POST("auth/signup")
    suspend fun createAccount(
        @Body createUserRequest: CreateUserRequest
    ): Response<CreateUserResponse>

    // Forgot password endpoint
    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body forgotPasswordRequest: ForgotPasswordRequest): Response<Unit>

    @POST("auth/reset-password")
    suspend fun resetPassword(@Body resetPasswordRequest: ResetPasswordRequest): Response<Void>
    @GET("auth/me")
    suspend fun getLoggedInUser(): Response<CreateUserRequest>
    @GET("auth/profile")
    suspend fun getUserInfo(@Query("email") email: String): Response<UserData>

}
