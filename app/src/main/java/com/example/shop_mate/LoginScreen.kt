package com.example.shop_mate

import UserViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: UserViewModel,
    onLogin: (String, String) -> Unit,
    onCreateAccountClick: () -> Unit // New parameter for Create Account navigation
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7EEE2))
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Image(
            painter = painterResource(id = R.drawable.logo), // Replace with your logo
            contentDescription = "Logo",
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Welcome Back",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF5D5C56)
        )
        Text(
            text = "Please log in to your account",
            fontSize = 16.sp,
            color = Color(0xFF999891)
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Email Text Field
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = if (!email.endsWith("@esprit.tn")) "Email must end with '@esprit.tn'" else ""
            },
            label = { Text("Email Address") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(8.dp),
            isError = emailError.isNotEmpty()
        )
        if (emailError.isNotEmpty()) {
            Text(text = emailError, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
        }

        // Password Text Field
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = if (!password.contains(Regex("^(?=.*[A-Z])(?=.*[!@#\$%^&+=]).{6,}$"))) {
                    "Password must contain at least one uppercase letter and one symbol"
                } else ""
            },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(8.dp),
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = "Toggle Password Visibility"
                    )
                }
            },
            isError = passwordError.isNotEmpty()
        )
        if (passwordError.isNotEmpty()) {
            Text(text = passwordError, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Login Button
        Button(
            onClick = { onLogin(email, password) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5D5C56)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(50.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Login", color = Color.White, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = { /* Handle Forgot Password Click */ }) {
            Text(text = "Forgot Password?", color = Color(0xFF5D5C56), fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Create Account Button
        TextButton(onClick = onCreateAccountClick) { // Calls onCreateAccountClick when clicked
            Text(text = "Don't have an account? Click here", color = Color(0xFF5D5C56), fontSize = 14.sp)
        }
    }
}

@Preview
@Composable
fun PreviewLoginScreen() {
    // Simulate a UserViewModel and a mock login action for preview
    val fakeViewModel = UserViewModel(UserRepository(RetrofitInstance.api)) // Simulate with mocks if necessary

    LoginScreen(
        viewModel = fakeViewModel,
        onLogin = { email, password ->
            // Mock action for preview
            println("Login attempt: Email = $email, Password = $password")
        },
        onCreateAccountClick = {
            // Mock action for navigating to Create Account
            println("Navigating to Create Account")
        }
    )
}
