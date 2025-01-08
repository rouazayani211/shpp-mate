package com.example.myapplication

import com.example.myapplication.Model.CreateUserRequest
import com.example.myapplication.Model.CreateUserResponse
import com.example.myapplication.Model.ForgotPasswordRequest
import com.example.myapplication.Model.LoginRequest
import com.example.myapplication.Model.LoginResponse
import com.example.myapplication.Model.ResetPasswordRequest
import com.example.myapplication.Model.UpdateUserRequest
import com.example.myapplication.Model.UserData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @GET("auth/profile")
    suspend fun getUserInfo(@Query("email") email: String): Response<UserData>


    @PUT("users/{id}")
    suspend fun updateAccount(
        @Path("id") id: String,
        @Body updateUserRequest: UpdateUserRequest
    ): Response<UserData>

    @Multipart
    @PUT("users/{id}")
    suspend fun updateAccountWithImage(
        @Path("id") id: String,
        @Body user : UserData,
        @Part file: MultipartBody.Part
    ): Response<UserData>
//    @Part("nom") nom: RequestBody,
//    @Part("prenom") prenom: RequestBody,
//    @Part("email") email: RequestBody,
//    @Part("password") password: RequestBody?,

    @GET("auth/profile/{id}")
    suspend fun getUserById(@Path("id") id: String): Response<UserData>
    @PATCH("user/add-to-history/{id}")
    suspend fun addToPurchaseHistory(
        @Path("id") userId: String,
        @Body items: List<String>
    ): Response<Unit>

}
