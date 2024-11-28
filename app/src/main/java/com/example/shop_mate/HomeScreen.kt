package com.example.shop_mate

import UserViewModel
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    produits: List<Produit>,
    isLoading: Boolean,
    errorMessage: String,
    loggedInUser: UserData // Utilisateur connecté
) {
    val context = LocalContext.current
    val searchQuery = remember { mutableStateOf("") }

    // Filtrer les produits en fonction de la recherche
    val filteredProduits = produits.filter {
        it.nom.contains(searchQuery.value, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7EEE2))
            .padding(24.dp)
    ) {
        // Barre supérieure avec logo, menu et image de profil
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            contentAlignment = Alignment.Center
        ) {
            // Logo centré
            Box(
                modifier = Modifier
                    .size(100.dp)
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
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 16.dp, top = 8.dp)
                    .size(24.dp)
                    .clickable { /* Gérer le clic sur le menu */ }
            )

            if (loggedInUser.imageProfile != null) {
                val url = "http://192.168.79.172:3000/uploads/${loggedInUser.imageProfile}"
                AsyncImage(
                    model = url,
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 16.dp, top = 8.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable {
                            val intent = Intent(context, ProfileActivity::class.java)
                            context.startActivity(intent)
                        },
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Titre aligné à gauche
        Text(
            text = "Match Your Style",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color(0xFF874F2C),

            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Barre de recherche personnalisée
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
                    painter = painterResource(id = R.drawable.ic_search), // Remplacez par votre icône de recherche
                    contentDescription = "Search Icon",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = searchQuery.value,
                    onValueChange = { searchQuery.value = it }, // Mettre à jour la recherche dynamique
                    placeholder = { Text("Search", color = Color.Gray) },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        textColor = Color.Black
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Trois boutons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { /* Gérer le clic du bouton Trending */ },
                modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAA8F5C))
            ) {
                Text("Trending Now", color = Color.White)
            }

            Button(
                onClick = { /* Gérer le clic du bouton New Arrivals */ },
                modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5D5C56))
            ) {
                Text("All", color = Color.White)
            }

            Button(
                onClick = { /* Gérer le clic du bouton Best Deals */ },
                modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDD4B5F))
            ) {
                Text("New", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = Color.Red)
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredProduits) { produit ->
                    ProductItem(produit)
                }
            }
        }
    }
}



@Composable
fun ProductItem(produit: Produit) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(12.dp)) // Fixed background color
            .padding(16.dp)
            .clickable {
                val intent = Intent(context, DetailsActivity::class.java).apply {
                    putExtra("PRODUCT_NAME", produit.nom)
                    putExtra("PRODUCT_PRICE", produit.prix)
                    putExtra("PRODUCT_IMAGE", produit.image)
                }
                context.startActivity(intent)
            }
    ) {
        val url = "http://192.168.79.172:3000/uploads/${produit.image}"

        // Product Image
        AsyncImage(
            model = url,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Product name and price
        Text(
            text = produit.nom,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        )

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
