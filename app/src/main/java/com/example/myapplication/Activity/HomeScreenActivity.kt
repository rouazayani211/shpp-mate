package com.example.myapplication.Activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.Model.Produit
import com.example.myapplication.Model.UserData
import com.example.myapplication.Network.RetrofitInstance
import com.example.myapplication.Screen.HomeScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class HomeScreenActivity : ComponentActivity() {

    // MutableState to store products, loading status, error message, and logged-in user
    private val produits = mutableStateOf<List<Produit>>(emptyList())
    private val isLoading = mutableStateOf(false)
    private val errorMessage = mutableStateOf("")
    private val loggedInUser = mutableStateOf<UserData?>(null) // Updated to use `UserData`

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            MaterialTheme {
                Scaffold(
                    bottomBar = { BottomNavigationBar(navController) }
                ) { paddingValues -> // Use paddingValues provided by Scaffold
                    Box(modifier = Modifier.padding(paddingValues)) {
                        NavigationHost(navController = navController)
                    }
                }
            }
        }

        // Fetch the logged-in user and products when the activity is created
        fetchLoggedInUser()
        fetchProduits()
    }

    // Function to fetch logged-in user details using `auth/profile`
    private fun fetchLoggedInUser() {
        val sharedPreferences = getSharedPreferences("shop_mate_prefs", MODE_PRIVATE)
        val email = sharedPreferences.getString("user_email", null)

        if (email.isNullOrEmpty()) {
            errorMessage.value = "User email is not available."
            return
        }

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val userResponse: Response<UserData> =
                    RetrofitInstance.userApi.getUserInfo(email)

                if (userResponse.isSuccessful) {
                    loggedInUser.value = userResponse.body()
                } else {
                    errorMessage.value =
                        "Failed to fetch user: ${userResponse.code()} - ${userResponse.message()}"
                }
            } catch (e: Exception) {
                errorMessage.value = "Error fetching user: ${e.localizedMessage}"
            }
        }
    }

    // Function to fetch products from the backend
    private fun fetchProduits() {
        isLoading.value = true
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response: Response<List<Produit>> = RetrofitInstance.produitApi.getProduits()
                if (response.isSuccessful) {
                    produits.value = response.body() ?: emptyList()
                } else {
                    errorMessage.value = "Error: ${response.code()} - ${response.message()}"
                }
            } catch (e: Exception) {
                errorMessage.value = "Exception fetching products: ${e.localizedMessage}"
            } finally {
                isLoading.value = false
            }
        }
    }


    // Bottom Navigation Bar
    @Composable
    fun BottomNavigationBar(navController: NavHostController) {
        val items = listOf(
            BottomNavItem.Home,
            BottomNavItem.Favorites,
            BottomNavItem.Settings,
            BottomNavItem.Profile,
            BottomNavItem.Cart


            )

        NavigationBar(
            containerColor = Color(0xFFF7EEE2),
            tonalElevation = 0.dp, // Remove elevation for a cleaner look
            modifier = Modifier.height(56.dp) // Adjust the height of the navigation bar
        ) {
            items.forEach { item ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            modifier = Modifier.size(20.dp), // Adjust icon size
                            tint = Color(0xFF874F2C)
                        )
                    },
                    label = {
                        Text(
                            text = item.title,
                            color = Color(0xFF874F2C),
                            style = MaterialTheme.typography.labelSmall // Smaller text style
                        )
                    },
                    selected = false,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }

    // Navigation Host
    @Composable
    fun NavigationHost(navController: NavHostController) {
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route
        ) {
            composable(BottomNavItem.Home.route) {
                HomeScreen(
                    produits = produits.value,
                    isLoading = isLoading.value,
                    errorMessage = errorMessage.value,
                    loggedInUser = loggedInUser.value ?: UserData(
                        id ="",
                        nom = "Unknown",
                        prenom = "User",
                        email = "unknown@example.com",
                        password = "",
                        imageProfile = ""
                    )
                )
            }
            composable(BottomNavItem.Favorites.route) {
                val context = LocalContext.current
                LaunchedEffect(Unit) {
                    val intent = Intent(context, FavoriteActivity::class.java)
                    context.startActivity(intent)            }
            }
            composable(BottomNavItem.Settings.route) {
                Text("Settings Screen", style = MaterialTheme.typography.headlineLarge)
            }
            composable(BottomNavItem.Profile.route) {
                val context = LocalContext.current
                LaunchedEffect(Unit) {
                    val intent = Intent(context, ProfileActivity::class.java)
                    context.startActivity(intent)
                }
            }
            composable(BottomNavItem.Cart.route) {
                val context = LocalContext.current
                LaunchedEffect(Unit) {
                    val intent = Intent(context, CartActivity::class.java)
                    context.startActivity(intent)
                }
            }
        }
    }

    // Data class for bottom navigation items
    data class BottomNavItem(
        val route: String,
        val icon: androidx.compose.ui.graphics.vector.ImageVector,
        val title: String
    ) {
        companion object {
            val Home = BottomNavItem("home", Icons.Default.Home, "Home")
            val Favorites = BottomNavItem("favorites", Icons.Default.Favorite, "Favorites")
            val Settings = BottomNavItem("settings", Icons.Default.Settings, "Settings")
            val Profile = BottomNavItem("profile", Icons.Default.Person, "Profile")
            val Cart=BottomNavItem("cart",Icons.Default.ShoppingCart,"Cat")
        }
    }
}

