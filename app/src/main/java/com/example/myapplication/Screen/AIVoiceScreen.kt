package com.example.myapplication.Screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.Model.AIRequest
import com.example.myapplication.Model.Message
import com.example.myapplication.Network.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.HttpException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIVoiceScreen() {
    var recognizedText by remember { mutableStateOf("") }
    var aiResponse by remember { mutableStateOf("Ask me anything!") }
    val coroutineScope = rememberCoroutineScope()

    // Define custom colors for the screen
    val backgroundColor = Color(0xFFF7EEE2) // Light beige
    val cardColor = Color(0xFFE4D2C5) // Light brown
    val buttonColor = Color(0xFFAA8F5C) // Medium brown
    val textColor = Color(0xFF5D4037) // Dark brown

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "AI Voice Assistant",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Input Field
        TextField(
            value = recognizedText,
            onValueChange = { recognizedText = it },
            label = { Text("Enter your question here") },
            modifier = Modifier
                .fillMaxWidth()
                .background(cardColor),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = cardColor,
                focusedIndicatorColor = buttonColor,
                unfocusedIndicatorColor = textColor.copy(alpha = 0.5f),
                focusedLabelColor = buttonColor,
                unfocusedLabelColor = textColor.copy(alpha = 0.6f),
                cursorColor = buttonColor
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Button to Send Query
        Button(
            onClick = {
                coroutineScope.launch {
                    aiResponse = "Fetching response..."
                    try {
                        val response = RetrofitInstance.geminiApi.generateContent(
                            AIRequest(
                                messages = listOf(
                                    Message(content = recognizedText, role = "user")
                                )
                            )
                        )
                        aiResponse = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                            ?: "No response received."
                    } catch (e: HttpException) {
                        val errorBody = e.response()?.errorBody()?.string()
                        aiResponse = "HTTP Error: ${e.code()} - ${errorBody ?: e.message()}"
                        Log.e("AIVoiceScreen", "HTTP Exception: $errorBody")
                    } catch (e: Exception) {
                        aiResponse = "Error: ${e.localizedMessage}"
                        Log.e("AIVoiceScreen", "Exception: ${e.localizedMessage}", e)
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Ask AI", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
        }

        Spacer(modifier = Modifier.height(20.dp))

        // AI Response Section
        Text(
            text = "AI Response:",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
        Text(
            text = aiResponse,
            fontSize = 16.sp,
            color = textColor,
            modifier = Modifier
                .padding(top = 8.dp)
                .background(cardColor, shape = MaterialTheme.shapes.medium)
                .padding(16.dp)
        )
    }
}
