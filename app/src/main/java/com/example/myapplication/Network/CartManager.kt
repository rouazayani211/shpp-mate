package com.example.myapplication.Network

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.myapplication.Model.CartItem
import com.example.myapplication.Network.CartManager.cartItems


object CartManager {
    val cartItems: SnapshotStateList<CartItem> = mutableStateListOf()

    fun addToCart(item: CartItem) {
        val existingIndex = cartItems.indexOfFirst {
            it.product.id == item.product.id && it.size == item.size && it.color == item.color
        }

        if (existingIndex != -1) {
            val existingItem = cartItems[existingIndex]
            val updatedItem = existingItem.copy(quantity = existingItem.quantity + item.quantity)
            cartItems[existingIndex] = updatedItem
        } else {
            cartItems.add(item)
        }
    }

    fun removeFromCart(index: Int) {
        if (index in cartItems.indices) {
            cartItems.removeAt(index)
        }
    }

    fun clearCart() {
        cartItems.clear()
    }
}


fun removeFromCart(index: Int) {
        if (index in cartItems.indices) {
            cartItems.removeAt(index)
        }
    }

    fun clearCart() {
        cartItems.clear()
    }

    fun getCartItemsNames(): List<String> {
        return cartItems.map { it.product.nom }
    }

