package com.example.shop_mate

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

val Brown = Color(0xFFA52A2A)

@Composable
fun DetailsScreen(

    name: String,
    price: Double,
    imageUrl: String,
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
            .padding(16.dp)
    ) {
        // Header with back button and title
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { (context as? android.app.Activity)?.finish() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Details",
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        // Product Image
        AsyncImage(
            model = imageUrl,
            contentDescription = "Product Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFF7EEE2))
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Product Name and Price
        Text(name, style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold))
        Text("$${price}", style = TextStyle(fontSize = 20.sp))

        Spacer(modifier = Modifier.height(16.dp))

        // Size Options
        Text("Size", style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            sizes.forEach { size ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(if (selectedSize == size) Color.Gray else Color.White, CircleShape)
                        .clickable { selectedSize = size }
                        .wrapContentSize(Alignment.Center)
                ) {
                    Text(size, style = TextStyle(fontSize = 14.sp))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Color Options
        Text("Colors", style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            colors.forEach { color ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(color, CircleShape)
                        .border(2.dp, if (selectedColor == color) Color.Black else Color.Transparent, CircleShape)
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
                        Produit(name, "Description", price, "Category", imageUrl) to (selectedSize to selectedColor!!)
                    )
                    context.startActivity(Intent(context, CartActivity::class.java))
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add to Cart", style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold))
        }
    }
}
