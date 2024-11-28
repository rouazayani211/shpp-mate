package com.example.shop_mate

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OTPVerificationScreen(
    onSubmit: (String, String) -> Unit
) {
    var otpValues by remember { mutableStateOf(List(6) { "" }) } // For 6-digit OTP
    var newPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7EEE2))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "Reset Password",
            fontSize = 24.sp,
            color = Color(0xFF5D5C56)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // OTP Fields
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            otpValues.forEachIndexed { index, value ->
                OutlinedTextField(
                    value = value,
                    onValueChange = { input ->
                        if (input.length <= 1 && input.matches(Regex("\\d*"))) {
                            otpValues = otpValues.toMutableList().apply {
                                this[index] = input
                            }
                        }
                    },
                    modifier = Modifier
                        .width(50.dp)
                        .height(60.dp),
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 24.sp,
                        color = Color.Black
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFFAA8F5C),
                        unfocusedBorderColor = Color(0xFFD4A276),
                        cursorColor = Color(0xFFAA8F5C)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // New Password Field
        OutlinedTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("New Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFFAA8F5C),
                unfocusedBorderColor = Color(0xFFD4A276),
                textColor = Color.Black,
                cursorColor = Color(0xFFAA8F5C)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Error Message
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Submit Button
        Button(
            onClick = {
                val resetToken = otpValues.joinToString("") // Combine OTP fields into a single string
                if (resetToken.length == 6 && newPassword.length >= 8) {
                    onSubmit(resetToken, newPassword)
                } else {
                    errorMessage = when {
                        resetToken.length != 6 -> "Please enter a valid 6-digit OTP."
                        newPassword.length < 8 -> "Password must be at least 8 characters."
                        else -> "Invalid input. Please try again."
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFAA8F5C),
                contentColor = Color.White
            )
        ) {
            Text(text = "Submit")
        }
    }
}
