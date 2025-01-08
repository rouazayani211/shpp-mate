package com.example.myapplication.Screen

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.myapplication.Activity.CartActivity
import com.example.myapplication.Activity.DetailsActivity
import com.example.myapplication.Activity.FavoriteActivity
import com.example.myapplication.Activity.ProfileActivity
import com.example.myapplication.Model.Produit
import com.example.myapplication.Model.UserData
import com.example.myapplication.Network.FavoriteManager
import com.example.myapplication.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    produits: List<Produit>,
    isLoading: Boolean,
    errorMessage: String,
    loggedInUser: UserData
) {
    val context = LocalContext.current
    val searchQuery = remember { mutableStateOf("") }
    val selectedCategory = remember { mutableStateOf("") } // Track the selected category

    // Initialize FavoriteManager
    LaunchedEffect(Unit) {
        FavoriteManager.init(context)
    }
    // Filter products dynamically based on search query and selected category
    val filteredProduits = produits.filter {
        (searchQuery.value.isEmpty() || it.nom.contains(searchQuery.value, ignoreCase = true)) &&
                (selectedCategory.value.isEmpty() || it.categorie == selectedCategory.value)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7EEE2))
            .padding(24.dp)
    ) {
        // Header with logo, menu icon, and profile image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(75.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFAA8F5C))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "App Logo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Cart",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 16.dp, top = 8.dp)
                    .size(24.dp)
                    .clickable {
                        val intent = Intent(context, CartActivity::class.java)
                        context.startActivity(intent)
                    },
                tint = Color(0xFF874F2C) // Same color as the bottom navigation bar icons
            )


            loggedInUser.imageProfile?.let { profileImage ->
                val url = "http://192.168.48.172:3000/uploads/$profileImage"
                AsyncImage(
                    model = url,
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 16.dp, top = 8.dp)
                        .size(35.dp)
                        .clip(CircleShape)
                        .clickable {
                            val intent = Intent(context, ProfileActivity::class.java)
                            context.startActivity(intent)
                        },
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Centered Title
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Match Your Style",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color(0xFF874F2C)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(Color(0xFFF4F2E9)),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "Search Icon",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = searchQuery.value,
                    onValueChange = { searchQuery.value = it },
                    placeholder = { Text("Search", color = Color.Gray) },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    textStyle = TextStyle(color = Color.Black),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Buttons for Man, Women, and Child with Toggle Behavior
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Button(
                    onClick = {
                        selectedCategory.value = if (selectedCategory.value == "Man") "" else "Man"
                    },
                    modifier = Modifier.padding(horizontal = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedCategory.value == "Man") Color(0xFFAA8F5C) else Color(0xFFF4F2E9)
                    )
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_man),
                            contentDescription = "Man Icon",
                            modifier = Modifier.size(18.dp),
                            tint = Color(0xFF874F2C)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Man", color = if (selectedCategory.value == "Man") Color.White else Color.Black)
                    }
                }
            }

            item {
                Button(
                    onClick = {
                        selectedCategory.value = if (selectedCategory.value == "Women") "" else "Women"
                    },
                    modifier = Modifier.padding(horizontal = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedCategory.value == "Women") Color(0xFFAA8F5C) else Color(0xFFF4F2E9)
                    )
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_women),
                            contentDescription = "Women Icon",
                            modifier = Modifier.size(18.dp),
                            tint = Color(0xFF874F2C)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Women", color = if (selectedCategory.value == "Women") Color.White else Color.Black)
                    }
                }
            }

            item {
                Button(
                    onClick = {
                        selectedCategory.value = if (selectedCategory.value == "Child") "" else "Child"
                    },
                    modifier = Modifier.padding(horizontal = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedCategory.value == "Child") Color(0xFFAA8F5C) else Color(0xFFF4F2E9)
                    )
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_child),
                            contentDescription = "Child Icon",
                            modifier = Modifier.size(18.dp),
                            tint = Color(0xFF874F2C)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Child", color = if (selectedCategory.value == "Child") Color.White else Color.Black)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display products in a vertical grid
        when {
            isLoading -> CircularProgressIndicator()
            errorMessage.isNotEmpty() -> Text(errorMessage, color = Color.Red)
            else -> LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredProduits) { produit ->
                    ProductItem(
                        produit = produit,
                        onFavoriteClick = { selectedProduct ->
                            val intent = Intent(context, FavoriteActivity::class.java).apply {
                                putExtra("PRODUCT_NAME", selectedProduct.nom)
                                putExtra("PRODUCT_PRICE", selectedProduct.prix)
                                putExtra("PRODUCT_IMAGE", selectedProduct.image)
                                putExtra("PRODUCT_DESCRIPTION", selectedProduct.description)
                                putExtra("PRODUCT_CATEGORY", selectedProduct.categorie)
                            }
                            context.startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}




@Composable
fun ProductItem(
    produit: Produit,
    onFavoriteClick: (Produit) -> Unit = {}
) {
    val context = LocalContext.current
    val isFavorite = remember { mutableStateOf(FavoriteManager.isFavorite(produit)) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
            .clickable {
                // Navigate to product details screen
                val intent = Intent(context, DetailsActivity::class.java).apply {
                    putExtra("PRODUCT_NAME", produit.nom)
                    putExtra("PRODUCT_PRICE", produit.prix)
                    putExtra("PRODUCT_IMAGE", produit.image)
                    putExtra("PRODUCT_DESCRIPTION", produit.description)
                    putExtra("PRODUCT_CATEGORY", produit.categorie)
                    putExtra("PRODUCT_ID", produit.id)
                }
                context.startActivity(intent)
            }
    ) {
        val url = "http://192.168.48.172:3000/uploads/${produit.image}"

        // Product Image
        AsyncImage(
            model = url,
            contentDescription = "Product Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = produit.nom,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Icon(
                imageVector = if (isFavorite.value) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Favorite",
                tint = Color.Red,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        if (isFavorite.value) {
                            // Remove from favorites
                            FavoriteManager.removeFromFavorites(produit)
                            isFavorite.value = false // Update local state
                        } else {
                            // Add to favorites
                            FavoriteManager.addToFavorites(produit)
                            isFavorite.value = true // Update local state
                        }
                    }
            )



        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "$${produit.prix}",
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
        )
    }
}





