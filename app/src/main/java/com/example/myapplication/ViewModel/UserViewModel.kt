package com.example.myapplication.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Model.LoginResponse
import com.example.myapplication.Model.UserData
import com.example.myapplication.Repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(private val repository: UserRepository) : ViewModel() {
    private val _currentUser = MutableLiveData<UserData?>()
    val currentUser: LiveData<UserData?> = _currentUser

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    // Expose the email of the logged-in user
    val loggedInUserEmail: LiveData<String?> = _currentUser.map { it?.email }

    // Function to set user data directly
    fun setUser(user: UserData) {
        _currentUser.value = user
    }

    // Login user and invoke the result callback
    fun loginUser(email: String, password: String, onResult: (Result<LoginResponse>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repository.loginUser(email, password)
                withContext(Dispatchers.Main) {
                    result.getOrNull()?.user?.let { setUser(it) } // Set user on successful login
                    onResult(result)
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Login failed: ${e.message}")
                withContext(Dispatchers.Main) {
                    onResult(Result.failure(e))
                }
            }
        }
    }

    // Fetch user data from the API using email
    fun fetchUserDataFromApi(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getUserInfo(email)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _currentUser.postValue(it) // Update user data
                    } ?: run {
                        Log.e("UserViewModel", "Empty response body")
                        _errorMessage.postValue("User data not found.")
                    }
                } else {
                    val error = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("UserViewModel", "API Error: $error")
                    _errorMessage.postValue("Failed to fetch user data: $error")
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Exception: ${e.message}")
                _errorMessage.postValue("An error occurred: ${e.message}")
            }
        }
    }

    // Clear the current user data (e.g., during logout)
    fun clearUserData() {
        _currentUser.value = null
    }
}
