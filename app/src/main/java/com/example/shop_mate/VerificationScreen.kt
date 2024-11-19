package com.example.shop_mate

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OTPVerificationScreen(onSubmit: (String) -> Unit) {
    var otpValues by remember { mutableStateOf(List(6) { "" }) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Verify Your Account",
            fontSize = 24.sp, // Larger title font size
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            otpValues.forEachIndexed { index, otp ->
                OutlinedTextField(
                    value = otp,
                    onValueChange = { value ->
                        if (value.length <= 1 && value.matches(Regex("\\d*"))) {
                            otpValues = otpValues.toMutableList().apply {
                                this[index] = value
                            }
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 24.sp // Larger font size for the OTP
                    ),
                    modifier = Modifier
                        .size(60.dp) // Larger size for each OTP field
                        .padding(4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp // Slightly larger error text size
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val otpCode = otpValues.joinToString("")
                if (otpCode.length == 6) {
                    onSubmit(otpCode)
                } else {
                    errorMessage = "Please enter a valid 6-digit OTP"
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp) // Larger button height
        ) {
            Text(text = "Submit", fontSize = 16.sp) // Slightly larger button text
        }
    }
}

@Preview
@Composable
fun PreviewOTPVerificationScreen() {
    MaterialTheme {
        OTPVerificationScreen { otpCode ->
            // Simulate submission (for preview only)
            println("Entered OTP: $otpCode")
        }
    }
}
