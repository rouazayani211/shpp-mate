package com.example.myapplication.Activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.myapplication.Screen.AIVoiceScreen

class VoiceToSearchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AIVoiceScreen() // Ensure this matches the Composable name
        }
    }
}
