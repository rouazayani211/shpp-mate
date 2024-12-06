package com.example.myapplication.Screen

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.myapplication.Activity.CartActivity
import com.example.myapplication.Activity.EditAccountActivity
import com.example.myapplication.Activity.HomeScreenActivity
import com.example.myapplication.Activity.LoginActivity
import com.example.myapplication.Model.UserData
import com.example.myapplication.R
import com.example.myapplication.ViewModel.UserViewModel


@Composable
fun ProfileScreen(viewModel: UserViewModel) {
    val user = viewModel.currentUser.observeAsState()
    val context = LocalContext.current // Get the context for navigation
    val userData = user.value // Assuming user is fetched from the ViewModel

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7EEE2)) // Palette color for background
    ) {
        // Back Icon
        Icon(
            painter = painterResource(id = R.drawable.ic_back), // Replace with your back icon drawable
            contentDescription = "Back",
            tint = Color(0xFF5D5C56), // Icon color
            modifier = Modifier
                .align(Alignment.TopStart) // Align at top-left
                .padding(16.dp)
                .size(24.dp)
                .clickable {
                    val intent = Intent(context, HomeScreenActivity::class.java)
                    context.startActivity(intent) // Navigate back to HomeScreenActivity
                }
        )

        // Top-Right Edit Profile Icon
        Icon(
            painter = painterResource(id = R.drawable.ic_edit),
            contentDescription = "Edit Profile",
            tint = Color(0xFF5D5C56),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(24.dp)
                .clickable {
                    // Corrected here: use context inside composable scope
                    val intent = Intent(context, EditAccountActivity::class.java)
                    userData?.let {
                        intent.putExtra("userData", it)  // Pass the UserData object
                        context.startActivity(intent)
                    }
                }
        )

        // Content for the profile screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            if (user.value != null) {
                val userData = user.value!!

                // Profile Image
                val url = "http://192.168.223.172:3000/uploads/${userData.imageProfile}"
                AsyncImage(
                    model = url,
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(200.dp)
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
                    ProfileButton("Your Orders", Icons.Default.ShoppingCart, Color(0xFF5D5C56)) {
                        val intent = Intent(context, CartActivity::class.java)
                        context.startActivity(intent) // Navigate to CartActivity
                    }

                    ProfileButton("Your Favourites", Icons.Default.Favorite, Color(0xFF5D5C56)) {
                        // Add navigation or functionality here
                    }

                    ProfileButton("Payment", Icons.Default.CreditCard, Color(0xFF5D5C56)) {
                        // Add navigation or functionality here
                    }

                    ProfileButton("Recommended Shops", Icons.Default.Place, Color(0xFF5D5C56)) {
                        // Add navigation or functionality here
                    }

                    ProfileButton("Nearest Shop", Icons.Default.LocationOn, Color(0xFF5D5C56)) {
                        // Add navigation or functionality here
                    }

                    ProfileButton("Settings", Icons.Default.Settings, Color(0xFF5D5C56)) {
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
                        contentColor = Color(0xFFAA8F5C)
                    ),
                    shape = RoundedCornerShape(50.dp),
                    border = BorderStroke(1.dp, Color(0xFFAA8F5C)) // Corrected border usage
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
