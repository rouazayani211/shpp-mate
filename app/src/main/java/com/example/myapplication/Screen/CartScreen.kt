package com.example.myapplication.Screen

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.myapplication.Activity.PaymentActivity
import com.example.myapplication.Model.CartItem
import com.example.myapplication.R

val CartScreenTitleColor = Color(0xFF874F2C)

@Composable
fun CartScreen(
    cartItems: SnapshotStateList<CartItem>,
    navigateToConfirmation: () -> Unit
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

        // Cart Items List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            itemsIndexed(cartItems) { index, cartItem ->
                CartItemRow(
                    cartItem = cartItem,
                    onQuantityChange = { newQuantity ->
                        if (newQuantity > 0) {
                            cartItems[index] = cartItem.copy(quantity = newQuantity)
                        } else {
                            itemToDeleteIndex = index
                            showDialog = true
                        }
                    },
                    onDelete = {
                        itemToDeleteIndex = index
                        showDialog = true
                    }
                )
            }
        }

        // Summary Section
        SummarySection(cartItems)

        Spacer(modifier = Modifier.height(16.dp))

        // Checkout Button
        Button(
            onClick = {
                val intent = Intent(context, PaymentActivity::class.java).apply {
                    putExtra("cartItems", ArrayList(cartItems))
                }
                context.startActivity(intent)
            },
            enabled = cartItems.isNotEmpty(),
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
    cartItem: CartItem,
    onQuantityChange: (Int) -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Product Image
        AsyncImage(
            model = cartItem.product.image,
            contentDescription = "Product Image",
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Product Details and Quantity Selector
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = cartItem.product.nom,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(text = "$${String.format("%.2f", cartItem.product.prix)}")
            Text(text = "Size: ${cartItem.size}")
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Color: ")
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(Color(cartItem.color), CircleShape) // Corrected usage
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Quantity Selector
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = { onQuantityChange(cartItem.quantity - 1) }) {
                    Icon(imageVector = Icons.Default.Remove, contentDescription = "Decrease Quantity")
                }
                Text(
                    text = cartItem.quantity.toString(),
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                )
                IconButton(onClick = { onQuantityChange(cartItem.quantity + 1) }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Increase Quantity")
                }
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
fun SummarySection(cartItems: SnapshotStateList<CartItem>) {
    val totalPrice = cartItems.sumOf { it.product.prix * it.quantity }
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Total:", fontWeight = FontWeight.Bold)
            Text(text = "$${String.format("%.2f", totalPrice)}")
        }
        Divider(color = Color.Gray, thickness = 1.dp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Grand Total:", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(text = "$${String.format("%.2f", totalPrice)}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
    }
}
