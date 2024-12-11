package com.example.myapplication.Screen

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.myapplication.Activity.HomeScreenActivity
import com.example.myapplication.Model.Produit
import com.example.myapplication.R

val CartScreenTitleColor = Color(0xFF874F2C)

@Composable
fun CartScreen(
    cartItems: SnapshotStateList<Pair<Produit, Pair<String, Color>>>,
    navigateToConfirmation: () -> Unit // Added parameter for navigation
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var itemToDeleteIndex by remember { mutableStateOf(-1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7EEE2))
            .padding(16.dp)
    ) {
        // Top Bar with Back Navigation
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { (context as? Activity)?.finish() } // Navigate back
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Back",
                    tint = Color(0xFF5D5C56),
                    modifier = Modifier
                        .padding(16.dp)
                        .size(24.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "My Cart",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = CartScreenTitleColor
                )
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        // Cart Items
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            itemsIndexed(cartItems) { index, (produit, details) ->
                CartItemRow(
                    produit = produit,
                    size = details.first,
                    color = details.second,
                    onDelete = {
                        showDialog = true
                        itemToDeleteIndex = index
                    }
                )
            }
        }

        // Summary Section
        val totalPrice = cartItems.sumOf { it.first.prix }
        SummarySection(totalPrice)

        Spacer(modifier = Modifier.height(16.dp))

        // Checkout Button
        Button(
            onClick = {}, // Trigger the navigation action
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAA8F5C))
        ) {
            Text(text = "Checkout", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }

    // Delete Confirmation Dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Delete Item") },
            text = { Text(text = "Are you sure you want to delete this item?") },
            confirmButton = {
                TextButton(onClick = {
                    cartItems.removeAt(itemToDeleteIndex)
                    showDialog = false
                }) {
                    Text(text = "OK", color = CartScreenTitleColor)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(text = "Cancel", color = Color.Gray)
                }
            }
        )
    }
}



@Composable
fun CartItemRow(
    produit: Produit,
    size: String,
    color: Color,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Product Image
        AsyncImage(
            model = produit.image,
            contentDescription = "Product Image",
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Product Details
        Column(modifier = Modifier.weight(1f)) {
            Text(text = produit.nom, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = "$${produit.prix}")
            Text(text = "Size: $size")
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Color: ")
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(color, CircleShape)
                )
            }
        }

        // Delete Icon
        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete Item",
                tint = CartScreenTitleColor
            )
        }
    }
}

@Composable
fun SummarySection(totalPrice: Double) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Total:", fontWeight = FontWeight.Bold)
            Text(text = "$${totalPrice}")
        }
        Divider(color = Color.Gray, thickness = 1.dp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Grand Total:", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(text = "$${totalPrice}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
    }
}

