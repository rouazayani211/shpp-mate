package com.example.myapplication.ViewModel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Model.UpdateUserRequest
import com.example.myapplication.Model.UserData
import com.example.myapplication.Network.RetrofitInstance
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class EditAccountViewModel(application: Application) : AndroidViewModel(application) {

    // EditAccountViewModel.kt

    // Fetch user data from the API
    fun fetchUserData(email: String, onFetched: (UserData?) -> Unit) {
        viewModelScope.launch {
            try {
                val api = RetrofitInstance.userApi
                val response = api.getUserInfo(email)
                if (response.isSuccessful) {
                    onFetched(response.body())
                } else {
                    onFetched(null)
                }
            } catch (e: Exception) {
                onFetched(null)
            }
        }
    }

    // Update user data in the API
    fun updateUserData(
        id: String,
        nom: String,
        prenom: String,
        email: String,
        password: String?,
        imageProfileUri: Uri?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val api = RetrofitInstance.userApi

                println("Updating user with ID: $id")
                println("Payload: nom=$nom, prenom=$prenom, email=$email, password=$password")

                val response = if (imageProfileUri != null) {
                    val userData=UserData(id, nom,prenom,email,password?: "",imageProfileUri.toString())
                    val fileName = imageProfileUri.lastPathSegment ?: "profile_image.jpg"
                    val inputStream = getApplication<Application>().contentResolver.openInputStream(imageProfileUri)
                    val imageBytes = inputStream?.readBytes()
                    val requestBody = imageBytes?.toRequestBody("image/*".toMediaTypeOrNull())
                    val imagePart = MultipartBody.Part.createFormData("file", fileName, requestBody!!)

                    api.updateAccountWithImage(
                        id,
                        user = userData,
                        imagePart
                    )
                } else {
                    api.updateAccount(
                        id,
                        UpdateUserRequest(
                            nom = nom,
                            prenom = prenom,
                            email = email,
                            password = password,
                            imageProfile = null
                        )
                    )
                }

                if (response.isSuccessful) {
                    println("Update successful: ${response.body()}")
                    onSuccess()
                } else {
                    val error = response.errorBody()?.string() ?: "Failed to update account"
                    println("Update failed: $error")
                    onError(error)
                }
            } catch (e: IOException) {
                println("Network error: ${e.message}")
                onError("Network error: ${e.message}")
            } catch (e: Exception) {
                println("Error: ${e.message}")
                onError("Error: ${e.message}")
            }
        }
    }



}