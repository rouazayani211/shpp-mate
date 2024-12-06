package com.example.myapplication.Screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAccountScreen(
    onSignInClick: (String, String, String, String, Uri?) -> Unit = { _, _, _, _, _ -> },
    onNavigateToLogin: () -> Unit = {}
) {
    var nom by remember { mutableStateOf("") }
    var prenom by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7EEE2))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 16.dp)
            )

            // Title
            Text(
                text = "Create Your Account",
                fontSize = 28.sp,
                color = Color(0xFF5D5C56),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Please fill in the details to get started",
                fontSize = 16.sp,
                color = Color(0xFF999891),
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Image Picker
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.Gray.copy(alpha = 0.2f))
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (selectedImageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(selectedImageUri),
                        contentDescription = "Selected Profile Picture",
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text(
                        text = "Upload Image",
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Input Fields
            OutlinedTextField(
                value = nom,
                onValueChange = { nom = it },
                label = { Text("Nom") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFFAA8F5C),
                    unfocusedBorderColor = Color(0xFFD4A276),
                    focusedLabelColor = Color(0xFFAA8F5C),
                    unfocusedLabelColor = Color(0xFFD4A276),
                    cursorColor = Color(0xFFAA8F5C),
                    containerColor = Color.Transparent
                ),
                textStyle = TextStyle(color = Color.Black)
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = prenom,
                onValueChange = { prenom = it },
                label = { Text("Prenom") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFFAA8F5C),
                    unfocusedBorderColor = Color(0xFFD4A276),
                    focusedLabelColor = Color(0xFFAA8F5C),
                    unfocusedLabelColor = Color(0xFFD4A276),
                    cursorColor = Color(0xFFAA8F5C),
                    containerColor = Color.Transparent
                ),
                textStyle = TextStyle(color = Color.Black)
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFFAA8F5C),
                    unfocusedBorderColor = Color(0xFFD4A276),
                    focusedLabelColor = Color(0xFFAA8F5C),
                    unfocusedLabelColor = Color(0xFFD4A276),
                    cursorColor = Color(0xFFAA8F5C),
                    containerColor = Color.Transparent
                ),
                textStyle = TextStyle(color = Color.Black)
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle Password Visibility"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFFAA8F5C),
                    unfocusedBorderColor = Color(0xFFD4A276),
                    focusedLabelColor = Color(0xFFAA8F5C),
                    unfocusedLabelColor = Color(0xFFD4A276),
                    cursorColor = Color(0xFFAA8F5C),
                    containerColor = Color.Transparent
                ),
                textStyle = TextStyle(color = Color.Black)
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Sign Up Button
            Button(
                onClick = {
                    onSignInClick(nom, prenom, email, password, selectedImageUri)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFAA8F5C),
                    contentColor = Color.White
                )
            ) {
                Text(text = "Sign Up", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Navigate to Login
            Text(
                text = "Already have an account? Sign In",
                color = Color(0xFF5D5C56),
                modifier = Modifier
                    .padding(top = 16.dp)
                    .clickable { onNavigateToLogin() },
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateAccountScreenPreview() {
    CreateAccountScreen()
}