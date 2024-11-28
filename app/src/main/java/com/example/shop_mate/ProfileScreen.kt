package com.example.shop_mate

import UserViewModel
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun ProfileScreen(viewModel: UserViewModel) {
    val user = viewModel.currentUser.observeAsState()
    val context = LocalContext.current // Get the context for navigation

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7EEE2)) // Palette color for background
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        if (user.value != null) {
            val userData = user.value!!

            // Profile Image
            val url = "http://192.168.79.172:3000/uploads/${userData.imageProfile}"
            AsyncImage(
                model = url,
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFAA8F5C)), // Palette color for placeholder
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            // User Info
            Text(
                text = "${userData.nom} ${userData.prenom}",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5D5C56) // Palette text color
            )
            Text(
                text = userData.email, // Display the user's email
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(60.dp))

            // Functional Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                // "Your Orders" Button
                ProfileButton("Your Orders", Icons.Default.ShoppingCart, Color(0xFF5D5C56)) {
                    // Add navigation or functionality here
                }

                // "Your Favourites" Button
                ProfileButton("Your Favourites", Icons.Default.Favorite, Color(0xFF5D5C56)) {
                    // Add navigation or functionality here
                }

                // "Payment" Button
                ProfileButton("Payment", Icons.Default.CreditCard, Color(0xFF5D5C56)) {
                    // Add navigation or functionality here
                }

                // "Recommended Shops" Button
                ProfileButton("Recommended Shops", Icons.Default.Place, Color(0xFF5D5C56)) {
                    // Add navigation or functionality here
                }

                // "Nearest Shop" Button
                ProfileButton("Nearest Shop", Icons.Default.LocationOn, Color(0xFF5D5C56)) {
                    // Add navigation or functionality here
                }

            }

                Spacer(modifier = Modifier.weight(1f))

            // Logout Button at the Bottom
            Button(
                onClick = {
                    logoutAndNavigateToLogin(context)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Red
                ),
                shape = RoundedCornerShape(50.dp),
                border = BorderStroke(1.dp, Color.Red) // Corrected border usage
            ) {
                Text(text = "Log Out", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

        } else {
            // Loading State
            CircularProgressIndicator(color = Color(0xFFAA8F5C))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Loading user data...",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
        }
    }
}




@Composable
fun ProfileButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = color
        )
    }
}


// Logout Function
fun logoutAndNavigateToLogin(context: Context) {
    val sharedPreferences = context.getSharedPreferences("shop_mate_prefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().clear().apply()

    val intent = Intent(context, LoginActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    context.startActivity(intent)
}
