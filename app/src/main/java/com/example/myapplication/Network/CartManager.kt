package com.example.myapplication.Network

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import com.example.myapplication.Model.Produit

object CartManager {
    private val _cartItems = mutableListOf<Pair<Produit, Pair<String, Color>>>()
    val cartItems: SnapshotStateList<Pair<Produit, Pair<String, Color>>> = mutableStateListOf()

    // Function to add items to the cart
    fun addToCart(item: Pair<Produit, Pair<String, Color>>) {
        cartItems.add(item)
    }

    fun removeFromCart(index: Int) {
        if (index in _cartItems.indices) {
            _cartItems.removeAt(index)
        }
    }

    fun clearCart() {
        _cartItems.clear()
    }
    fun getCartItemsNames(): List<String> {
        return cartItems.map { it.first.nom }
    }

}
