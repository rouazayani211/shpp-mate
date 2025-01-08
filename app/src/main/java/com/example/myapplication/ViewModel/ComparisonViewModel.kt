package com.example.myapplication.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Model.ComparisonResult
import com.example.myapplication.Network.RetrofitInstance.api
import kotlinx.coroutines.launch
import androidx.compose.runtime.State  // Add this import for State


class ComparisonViewModel : ViewModel() {
    private val _comparisonData = mutableStateOf<ComparisonResult?>(null)
    val comparisonData: State<ComparisonResult?> = _comparisonData

    fun fetchComparisonData(productId: String) {
        viewModelScope.launch {
            val response = api.compareProduct(productId)
            if (response.isSuccessful) {
                _comparisonData.value = response.body()
            } else {
                // Handle failure
            }
        }
    }
}