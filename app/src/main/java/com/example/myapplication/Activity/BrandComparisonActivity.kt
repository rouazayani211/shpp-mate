package com.example.myapplication.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.myapplication.Screen.ZaraMapScreen

class BrandComparisonActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Call the composable function to display the comparison UI
            ZaraMapScreen()
        }
    }
}

