package com.example.myapplication.ViewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Model.ComparisonResult
import com.example.myapplication.Model.Produit
import com.example.myapplication.Network.RetrofitInstance
import com.example.myapplication.ProduitApiService
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductViewModel : ViewModel() {
    private val _comparisonResult = MutableLiveData<ComparisonResult>()
    val comparisonResult: LiveData<ComparisonResult> get() = _comparisonResult

    private val apiService = Retrofit.Builder()
        .baseUrl("http://192.168.7.172:3000")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ProduitApiService::class.java)

    fun getComparison(id: String) {
        viewModelScope.launch {
            val response = apiService.compareProduct(id)
            if (response.isSuccessful) {
                _comparisonResult.value = response.body()
            } else {
                // Handle error
                Log.e("ProductViewModel", "Error: ${response.message()}")
            }
        }
    }
}
