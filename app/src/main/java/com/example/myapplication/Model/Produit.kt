package com.example.myapplication.Model
import androidx.compose.ui.graphics.Color
import java.io.Serializable

data class Produit(
    val id: String,
    val nom: String,
    val description: String,
    val prix: Double,
    val categorie: String,
    val image: String,
    val promotionPourcentage: Double? = null, // Optional promotion percentage
    val discountedPrice: Double? = null // Optional discounted price
) : Serializable
