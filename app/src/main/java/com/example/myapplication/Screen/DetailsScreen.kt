package com.example.myapplication.Screen

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.myapplication.Activity.CartActivity
import com.example.myapplication.Activity.ProfileActivity
import com.example.myapplication.Model.Produit
import com.example.myapplication.Network.CartManager
import com.example.myapplication.R

val Brown = Color(0xFFA52A2A)

@Composable
fun DetailsScreen(
    name: String,
    price: Double,
    imageUrl: String,
    profileUrl: String?, // Profile image URL (nullable in case it's not available)
    sizes: List<String> = listOf("S", "M", "L", "XL"),
    colors: List<Color> = listOf(Color.Red, Color.Blue, Brown, Color.Green, Color.Black)
) {
    val context = LocalContext.current
    var selectedSize by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf<Color?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7EEE2))
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Header Section with logo, menu, and profile image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            contentAlignment = Alignment.Center
        ) {
            // Logo centered
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

            // Menu Icon
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 16.dp, top = 8.dp)
                    .size(24.dp)
                    .clickable { /* Handle menu click */ }
            )

            // Profile Image
            if (profileUrl != null) {
                AsyncImage(
                    model = profileUrl,
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
        Spacer(modifier = Modifier.height(25.dp))


        // Product Image
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center // Centers the content horizontally
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Product Image",
                modifier = Modifier
                    .height(320.dp)
                    .background(Color.LightGray)
            )
        }


        // Product Name and Price
        Column(modifier = Modifier.padding(16.dp)) {
            Text(name, style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(4.dp))
            Text("$${price}", style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Size Options
        Text(
            text = "Size",
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            sizes.forEach { size ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            if (selectedSize == size) Color.Gray else Color.White,
                            CircleShape
                        )
                        .clickable { selectedSize = size }
                        .wrapContentSize(Alignment.Center)
                ) {
                    Text(size, style = TextStyle(fontSize = 14.sp))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Color Options
        Text(
            text = "Colors",
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            colors.forEach { color ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(color, CircleShape)
                        .border(
                            4.dp,
                            if (selectedColor == color) Color.White else Color.Transparent,
                            CircleShape
                        )
                        .clickable { selectedColor = color }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Add to Cart Button
        Button(
            onClick = {
                if (selectedSize.isNotEmpty() && selectedColor != null) {
                    CartManager.addToCart(
                        Produit(
                            id = "some_unique_id", // Assign a unique id
                            nom = name,
                            description = "Product Description",
                            prix = price,
                            categorie = "Product Category",
                            image = imageUrl
                        ) to (selectedSize to selectedColor!!)
                    )
                    context.startActivity(Intent(context, CartActivity::class.java))
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFAA8F5C),
                contentColor = Color.White
            )
        ) {
            Text("Add To Cart", style = TextStyle(fontWeight = FontWeight.Bold))
        }
    }
}
