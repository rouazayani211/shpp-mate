package com.example.myapplication.Activity

// Imports (already present in your code)
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import com.example.myapplication.Screen.MapDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class HomeScreenActivity : ComponentActivity() {

    // Existing State
    private val produits = mutableStateOf<List<Produit>>(emptyList())
    private val isLoading = mutableStateOf(false)
    private val errorMessage = mutableStateOf("")
    private val loggedInUser = mutableStateOf<UserData?>(null)
    private val showMapDialog = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val isFabMenuOpen = remember { mutableStateOf(false) } // State for FAB menu visibility

            MaterialTheme {
                Scaffold(
                    floatingActionButton = {
                        FloatingActionMenu(
                            isFabMenuOpen = isFabMenuOpen,
                            onFabClick = { isFabMenuOpen.value = !isFabMenuOpen.value },
                            onActionClick = { action ->
                                // Handle FAB menu item actions
                                when (action) {
                                    "Cart" -> navigateCart()
                                    "Profile" -> navigateprofile()
                                    "Favorites" -> navigatefavorites()
                                    "Maps" -> showMapDialog.value = true
                                    "Microphone" ->  navigateToAIVoiceActivity()
                                }
                            }
                        )
                    }
                ) { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues)) {
                        NavigationHost(navController = navController)

                        if (showMapDialog.value) {
                            MapDialog(
                                onDismiss = { showMapDialog.value = false },  // Close the map dialog
                                onLocationSelected = { selectedLocation ->
                                    Log.e("TAG", "Selected location: $selectedLocation")
                                    showMapDialog.value = false  // Close the map dialog after selection
                                }
                            )
                        }
                    }
                }
            }
        }

        fetchLoggedInUser()
        fetchProduits()
    }

    private fun fetchLoggedInUser() {
        val sharedPreferences = getSharedPreferences("shop_mate_prefs", MODE_PRIVATE)
        val email = sharedPreferences.getString("user_email", null)

        if (email.isNullOrEmpty()) {
            errorMessage.value = "User email is not available."
            return
        }

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val userResponse: Response<UserData> = RetrofitInstance.userApi.getUserInfo(email)
                if (userResponse.isSuccessful) {
                    loggedInUser.value = userResponse.body()
                } else {
                    errorMessage.value = "Failed to fetch user: ${userResponse.code()} - ${userResponse.message()}"
                }
            } catch (e: Exception) {
                errorMessage.value = "Error fetching user: ${e.localizedMessage}"
            }
        }
    }

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

//    private fun navigateToMaps() {
//        // Navigate to a Maps Activity or Screen
//        showMapDialog = true
//        if (showMapDialog) {
//            MapDialog(
//                onDismiss = { showMapDialog = false },
//                onLocationSelected = { selectedLocation ->
//                    Log.e("TAG", "Selected location: $selectedLocation")
//                    showMapDialog = false
//                }
//            )
//        }
//    }

    private fun handleMicrophone() {
        // Implement Microphone functionality here
        // Example: Show a toast or start a recording activity
    }

    @Composable
    fun FloatingActionMenu(
        isFabMenuOpen: MutableState<Boolean>,
        onFabClick: () -> Unit,
        onActionClick: (String) -> Unit
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {
            AnimatedVisibility(
                visible = isFabMenuOpen.value,
                enter = fadeIn() + slideInVertically { it },
                exit = fadeOut() + slideOutVertically { it }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(end = 16.dp, bottom = 72.dp)
                ) {
                    ActionButton(Icons.Default.ShoppingCart, "Cart") { onActionClick("Cart") }
                    ActionButton(Icons.Default.Person, "Profile") { onActionClick("Profile") }
                    ActionButton(Icons.Default.Favorite, "Favorites") { onActionClick("Favorites") }
                    ActionButton(Icons.Default.Map, "Maps") { onActionClick("Maps") }
                    ActionButton(Icons.Default.Mic, "Microphone") { onActionClick("Microphone") }
                }
            }

            FloatingActionButton(
                onClick = onFabClick,
                shape = CircleShape,
                containerColor = Color(0xFF874F2C)
            ) {
                Icon(
                    imageVector = if (isFabMenuOpen.value) Icons.Default.Close else Icons.Default.Menu,
                    contentDescription = "FAB Menu",
                    tint = Color.White
                )
            }
        }
    }

    @Composable
    fun ActionButton(icon: androidx.compose.ui.graphics.vector.ImageVector, description: String, onClick: () -> Unit) {
        FloatingActionButton(
            onClick = onClick,
            shape = CircleShape,
            modifier = Modifier.size(48.dp),
            containerColor = Color(0xFFF7EEE2)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = description,
                tint = Color(0xFF874F2C)
            )
        }
    }

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
                        id = "",
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
                    context.startActivity(intent)
                }
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

    data class BottomNavItem(
        val route: String,
        val icon: androidx.compose.ui.graphics.vector.ImageVector,
        val title: String
    ) {
        companion object {
            val Home = BottomNavItem("home", Icons.Default.Home, "Home")
            val Favorites = BottomNavItem("favorites", Icons.Default.Favorite, "Favorites")
            val Profile = BottomNavItem("profile", Icons.Default.Person, "Profile")
            val Cart = BottomNavItem("cart", Icons.Default.ShoppingCart, "Cart")
        }
    }
    private fun navigatefavorites() {
        val intent = Intent(this, FavoriteActivity::class.java)
        startActivity(intent)
    }
    private fun navigateprofile() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }
    private fun navigateCart() {
        val intent = Intent(this, CartActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToAIVoiceActivity() {
        val intent = Intent(this, VoiceToSearchActivity::class.java)
        startActivity(intent)
    }
}
