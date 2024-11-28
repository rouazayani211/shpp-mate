package com.example.shop_mate

import UserViewModel
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: UserViewModel,
    onLogin: (String, String) -> Unit,
    onCreateAccountClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onGoogleSignIn: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var rememberMeChecked by remember { mutableStateOf(false) } // State for the checkbox

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7EEE2)) // Background color matching the app theme
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // App Logo
        Image(
            painter = painterResource(id = R.drawable.logo), // Replace with your logo
            contentDescription = "App Logo",
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Welcome Text
        Text(
            text = "Welcome Back!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF5D5C56)
        )
        Text(
            text = "Please login to your account",
            fontSize = 16.sp,
            color = Color(0xFF999891)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Email TextField
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = if (!email.contains("@")) "Invalid email address" else ""
            },
            label = { Text("Email Address") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
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

        // Password TextField
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = if (password.length < 6) "Password must be at least 6 characters" else ""
            },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            isError = passwordError.isNotEmpty(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFFAA8F5C),
                unfocusedBorderColor = Color(0xFFD4A276),
                textColor = Color.Black,
                cursorColor = Color(0xFFAA8F5C)
            )
        )
        if (passwordError.isNotEmpty()) {
            Text(text = passwordError, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Remember Me Checkbox
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Checkbox(
                checked = rememberMeChecked,
                onCheckedChange = { rememberMeChecked = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFFAA8F5C),
                    uncheckedColor = Color(0xFFD4A276)
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Remember Me",
                fontSize = 14.sp,
                color = Color(0xFF5D5C56)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Login Button
        Button(
            onClick = { onLogin(email, password) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFAA8F5C),
                contentColor = Color.White
            )
        ) {
            Text(text = "Login", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))



        // Google Sign-In Button
        Button(
            onClick = onGoogleSignIn,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google_logo), // Replace with your Google logo
                    contentDescription = "Google Logo",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Sign in with Google", fontSize = 16.sp)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Forgot Password Link
        TextButton(onClick = onForgotPasswordClick) {
            Text(
                text = "Forgot Password?",
                color = Color(0xFF5D5C56),
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


        // Create Account Link
        TextButton(onClick = onCreateAccountClick) {
            Text(
                text = "Don't have an account? Create one",
                color = Color(0xFF5D5C56),
                fontSize = 14.sp
            )
        }
    }
}





@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(
        viewModel = UserViewModel(UserRepository(RetrofitInstance.userApi)), // Mock ViewModel
        onLogin = { _, _ -> },
        onCreateAccountClick = {},
        onForgotPasswordClick = {},
        onGoogleSignIn = {}
    )
}
