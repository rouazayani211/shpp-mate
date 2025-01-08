package com.example.myapplication.Screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.Model.CartItem
import com.example.myapplication.Network.RetrofitInstance
import com.example.myapplication.ReceiptRequest
import com.example.myapplication.ViewModel.CartViewModel
import com.example.myapplication.ViewModel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun PaymentScreen(
    userViewModel: UserViewModel,
    cartViewModel: CartViewModel, // Pass CartViewModel to manage the cart
    cartItems: List<CartItem>,
    onPaymentSuccess: () -> Unit
) {
    val userEmail by userViewModel.loggedInUserEmail.observeAsState(null)
    val emailForPayment = userEmail ?: "No Email Found"

    Log.d("PaymentScreen", "User email: $emailForPayment")

    var cardPart1 by remember { mutableStateOf("") }
    var cardPart2 by remember { mutableStateOf("") }
    var cardPart3 by remember { mutableStateOf("") }
    var cardPart4 by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    var isProcessing by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    val totalPrice = cartItems.sumOf { it.product.prix * it.quantity }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7EEE2))
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Header
            Text(
                text = "Payment",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5D5C56)
            )
            Text(
                text = "Email: $emailForPayment",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray
            )
            Text(
                text = "Total: $${String.format("%.2f", totalPrice)}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF874F2C)
            )

            // Card Number Fields
            Text("Card Number", fontSize = 14.sp, color = Color.Gray)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val cardParts = listOf(cardPart1, cardPart2, cardPart3, cardPart4)
                val onPartChange = listOf<(String) -> Unit>(
                    { if (it.length <= 4) cardPart1 = it },
                    { if (it.length <= 4) cardPart2 = it },
                    { if (it.length <= 4) cardPart3 = it },
                    { if (it.length <= 4) cardPart4 = it }
                )
                cardParts.zip(onPartChange).forEach { (part, onChange) ->
                    OutlinedTextField(
                        value = part,
                        onValueChange = onChange,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Expiry Date and CVV
            Text("Expiry Date (MM/YY) & CVV", fontSize = 14.sp, color = Color.Gray)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = expiryDate,
                    onValueChange = { if (it.length <= 5) expiryDate = formatExpiryDate(it) },
                    label = { Text("MM/YY") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = cvv,
                    onValueChange = { if (it.length <= 3) cvv = it },
                    label = { Text("CVV") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
            }

            // Pay Now Button
            Button(
                onClick = {
                    val fullCardNumber = cardPart1 + cardPart2 + cardPart3 + cardPart4
                    if (validateInputs(fullCardNumber, expiryDate, cvv)) {
                        isProcessing = true
                        sendReceiptEmailAndSavePurchase(
                            emailForPayment,
                            cartItems,
                            totalPrice
                        ) { success ->
                            isProcessing = false
                            dialogMessage = if (success) {
                                cartViewModel.clearCart() // Clear the cart safely
                                "Payment Successful"
                            } else {
                                "Payment Failed"
                            }
                            showDialog = true
                        }
                    } else {
                        dialogMessage = "Invalid payment details. Please check your inputs."
                        showDialog = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA57E5E)),
                enabled = !isProcessing
            ) {
                if (isProcessing) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Pay Now", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

        }
    }

    // Payment Status Dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Payment Status") },
            text = { Text(dialogMessage) },
            confirmButton = {
                Button(onClick = {
                    showDialog = false
                    if (dialogMessage.contains("Successful", ignoreCase = true)) {
                        onPaymentSuccess()
                    }
                }) {
                    Text("OK")
                }
            }
        )
    }
}


fun sendReceiptEmailAndSavePurchase(
    userEmail: String,
    cartItems: List<CartItem>,
    totalPrice: Double,
    onComplete: (Boolean) -> Unit
) {
    val api = RetrofitInstance.paymentApi
    val currentDate = java.text.SimpleDateFormat("yyyy-MM-dd").format(java.util.Date())
    val purchaseDetails = "$currentDate: Total $${String.format("%.2f", totalPrice)}"

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val receiptRequest = ReceiptRequest(
                email = userEmail,
                products = cartItems.map { it.product },
                total = totalPrice
            )

            // Send the receipt email
            val emailResponse = api.sendReceipt(receiptRequest)
            if (emailResponse.isSuccessful) {
                Log.d("Payment", "Email sent successfully: ${emailResponse.body()?.message}")
            } else {
                Log.e("Payment", "Failed to send email: ${emailResponse.errorBody()?.string()}")
            }

            // Save purchase history
            val historyResponse = api.addPurchaseToHistory(
                mapOf("email" to userEmail, "purchaseDetails" to purchaseDetails)
            )
            if (historyResponse.isSuccessful) {
                Log.d("Payment", "Purchase history saved successfully")
                onComplete(true)
            } else {
                Log.e("Payment", "Failed to save purchase history: ${historyResponse.errorBody()?.string()}")
                onComplete(false)
            }
        } catch (e: Exception) {
            Log.e("Payment", "Error during payment: ${e.message}", e)
            onComplete(false)
        }
    }
}

fun formatExpiryDate(input: String): String {
    val digitsOnly = input.filter { it.isDigit() }
    return when {
        digitsOnly.length <= 2 -> digitsOnly
        else -> "${digitsOnly.substring(0, 2)}/${digitsOnly.substring(2, digitsOnly.length.coerceAtMost(4))}"
    }
}

fun validateInputs(cardNumber: String, expiry: String, cvv: String): Boolean {
    val expiryRegex = Regex("""^(0[1-9]|1[0-2])/\d{2}$""") // MM/YY format
    val cardNumberValid = cardNumber.length == 16 && cardNumber.all { it.isDigit() }
    val expiryValid = expiry.matches(expiryRegex)
    val cvvValid = cvv.length == 3 && cvv.all { it.isDigit() }

    return cardNumberValid && expiryValid && cvvValid
}
