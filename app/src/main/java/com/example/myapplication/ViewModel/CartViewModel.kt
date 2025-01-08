package com.example.myapplication.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.Model.CartItem

class CartViewModel : ViewModel() {
    private val _cartItems = MutableLiveData<List<CartItem>>(emptyList())
    val cartItems: LiveData<List<CartItem>> get() = _cartItems

    // Add an item to the cart
    fun addToCart(item: CartItem) {
        val updatedCart = _cartItems.value?.toMutableList() ?: mutableListOf()
        updatedCart.add(item)
        _cartItems.postValue(updatedCart)
    }

    // Remove an item from the cart
    fun removeFromCart(index: Int) {
        val updatedCart = _cartItems.value?.toMutableList()
        updatedCart?.removeAt(index)
        _cartItems.postValue(updatedCart ?: emptyList())
    }

    // Update the quantity of an item
    fun updateCartItem(index: Int, updatedItem: CartItem) {
        val updatedCart = _cartItems.value?.toMutableList()
        updatedCart?.set(index, updatedItem)
        _cartItems.postValue(updatedCart ?: emptyList())
    }

    // Clear the cart
    fun clearCart() {
        _cartItems.postValue(emptyList())
    }
}
