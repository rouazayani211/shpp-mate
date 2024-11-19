package com.example.shop_mate

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@OptIn(ExperimentalMaterial3Api::class) // Opt into Material3 experimental API
@Composable
fun ForgotPasswordScreen(onSubmit: (String) -> Unit) {
    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Forgot Password", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = if (!android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()) {
                    "Invalid email address"
                } else ""
            },
            label = { Text("Email") },
            isError = emailError.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        )
        if (emailError.isNotEmpty()) {
            Text(text = emailError, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { if (emailError.isEmpty()) onSubmit(email) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Send Code")
        }
    }
}
