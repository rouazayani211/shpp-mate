    package com.example.myapplication.Network

    import androidx.compose.runtime.mutableStateListOf
    import com.example.myapplication.Model.Produit
    import android.content.Context
    import android.content.SharedPreferences
    import com.google.gson.Gson
    import com.google.gson.reflect.TypeToken

    object FavoriteManager {

        private const val PREF_NAME = "FavoritesPref"
        private const val FAVORITES_KEY = "favorites"

        private lateinit var sharedPreferences: SharedPreferences
        private val gson = Gson()

        // This will hold the current favorite list
        val favoriteItems = mutableStateListOf<Produit>()

        // Initialize SharedPreferences
        fun init(context: Context) {
            sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            loadFavorites()
        }

        // Add item to favorites
        fun addToFavorites(produit: Produit) {
            if (!favoriteItems.any { it.id == produit.id }) {
                favoriteItems.add(produit)
                saveFavorites()
            }
        }

        // Remove item from favorites
        fun removeFromFavorites(produit: Produit) {
            favoriteItems.removeIf { it.id == produit.id }
            saveFavorites()
        }

        // Check if the product is a favorite
        fun isFavorite(produit: Produit): Boolean {
            return favoriteItems.any { it.id == produit.id }
        }

        // Load favorites from SharedPreferences
        private fun loadFavorites() {
            val json = sharedPreferences.getString(FAVORITES_KEY, null) ?: "[]"
            val type = object : TypeToken<List<Produit>>() {}.type
            val savedFavorites: List<Produit> = try {
                gson.fromJson(json, type)
            } catch (e: Exception) {
                emptyList()
            }
            favoriteItems.clear()
            favoriteItems.addAll(savedFavorites)
        }


        // Save favorites to SharedPreferences
        private fun saveFavorites() {
            val json = gson.toJson(favoriteItems)
            Thread {
                sharedPreferences.edit().putString(FAVORITES_KEY, json).apply()
            }.start()
        }

    }
