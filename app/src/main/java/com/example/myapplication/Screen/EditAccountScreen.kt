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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.Model.UserData
import com.example.myapplication.R
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAccountScreen(
    userData: UserData?, // Accept the user data as a parameter
    onSaveChanges: (String, String, String, String, Uri?) -> Unit
) {
    // Initialize fields with userData values
    var nom by remember { mutableStateOf(userData?.nom.orEmpty()) }
    var prenom by remember { mutableStateOf(userData?.prenom.orEmpty()) }
    var email by remember { mutableStateOf(userData?.email.orEmpty()) }
    var password by remember { mutableStateOf(userData?.password.orEmpty()) }
    var imageProfileUri by remember { mutableStateOf<Uri?>(null) }

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> imageProfileUri = uri }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7EEE2)) // Same background color as CreateAccountScreen
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo Section
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 16.dp)
            )

            // Title Section
            Text(
                text = "Edit Your Account",
                fontSize = 28.sp,
                color = Color(0xFF5D5C56), // Use the same title color
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = "Modify your information below",
                fontSize = 16.sp,
                color = Color(0xFF999891), // Same subtitle color as CreateAccountScreen
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Profile Image Section
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.Gray.copy(alpha = 0.2f))
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (imageProfileUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(imageProfileUri),
                        contentDescription = "Selected Profile Picture",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    if (!userData?.imageProfile.isNullOrEmpty()) {
                        Image(
                            painter = rememberAsyncImagePainter("http://192.168.48.172:3000/uploads/${userData!!.imageProfile}"),
                            contentDescription = "Profile Picture",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text(
                            text = "Upload Image",
                            color = Color.Black,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Input Fields
            OutlinedTextField(
                value = nom,
                onValueChange = { nom = it },
                label = { Text("First Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFFAA8F5C),
                    unfocusedBorderColor = Color(0xFFD4A276),
                    focusedLabelColor = Color(0xFFAA8F5C),
                    unfocusedLabelColor = Color(0xFFD4A276),
                    cursorColor = Color(0xFFAA8F5C),
                    containerColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = prenom,
                onValueChange = { prenom = it },
                label = { Text("Last Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFFAA8F5C),
                    unfocusedBorderColor = Color(0xFFD4A276),
                    focusedLabelColor = Color(0xFFAA8F5C),
                    unfocusedLabelColor = Color(0xFFD4A276),
                    cursorColor = Color(0xFFAA8F5C),
                    containerColor = Color.Transparent
                )
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
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFFAA8F5C),
                    unfocusedBorderColor = Color(0xFFD4A276),
                    focusedLabelColor = Color(0xFFAA8F5C),
                    unfocusedLabelColor = Color(0xFFD4A276),
                    cursorColor = Color(0xFFAA8F5C),
                    containerColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Save Changes Button
            Button(
                onClick = { onSaveChanges(nom, prenom, email, password, imageProfileUri) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFAA8F5C),
                    contentColor = Color.White
                )
            ) {
                Text(text = "Save Changes", fontSize = 18.sp)
            }
        }
    }
}
