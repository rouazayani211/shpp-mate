package com.example.myapplication.Activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.myapplication.Model.Produit
import com.example.myapplication.Network.FavoriteManager
import com.example.myapplication.Screen.FavoriteActivityScreen

class FavoriteActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize FavoriteManager with the context
        FavoriteManager.init(this)

        // Set the content to display favorite products
        setContent {
            FavoriteActivityScreen()
        }
    }
}
