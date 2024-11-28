package com.example.shop_mate

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(onSubmit: (String) -> Unit) {
    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7EEE2)) // Match background color with LoginScreen
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Logo
        Image(
            painter = painterResource(id = R.drawable.logo), // Replace with your actual logo resource
            contentDescription = "App Logo",
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Title
        Text(
            text = "Forgot Password",
            fontSize = 24.sp,
            color = Color(0xFF5D5C56),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Email Input
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = if (!android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()) {
                    "Invalid email address"
                } else ""
            },
            label = { Text("Email Address") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(12.dp),
            isError = emailError.isNotEmpty(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFFAA8F5C),
                unfocusedBorderColor = Color(0xFFD4A276),
                textColor = Color.Black,
                cursorColor = Color(0xFFAA8F5C)
            )
        )
        if (emailError.isNotEmpty()) {
            Text(text = emailError, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Send Code Button
        Button(
            onClick = { if (emailError.isEmpty()) onSubmit(email) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFAA8F5C),
                contentColor = Color.White
            )
        ) {
            Text(text = "Send Code", fontSize = 18.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewForgotPasswordScreen() {
    ForgotPasswordScreen(onSubmit = {})
}
