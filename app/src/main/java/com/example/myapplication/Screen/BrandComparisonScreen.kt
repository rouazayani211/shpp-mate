package com.example.myapplication.Screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState


@Composable
fun ZaraMapScreen() {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(LatLng(36.8065, 10.1815), 12f, 0f, 0f) // Default: Tunis
    }

    var showPriceComparator by remember { mutableStateOf(false) }
    var selectedZara by remember { mutableStateOf<Brand?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            cameraPositionState = cameraPositionState,
            modifier = Modifier.fillMaxSize(),
            onMapClick = { latLng ->
                // Handle map click if needed
            }
        ) {
            // Markers for Zara locations
            zaraLocations.forEach { zara ->
                Marker(
                    state = rememberMarkerState(position = LatLng(zara.latitude, zara.longitude)),
                    title = zara.name,
                    onClick = {
                        selectedZara = zara
                        showPriceComparator = true
                        true
                    }
                )
            }
        }

        // Show Price Comparator Popup
        if (showPriceComparator && selectedZara != null) {
            ZaraPriceComparisonPopup(
                title = selectedZara!!.name,
                comparisonData = listOf(
                    ProductComparison("Shirt", "50,000 TND", "45,000 TND", "Competitor A"),
                    ProductComparison("Jeans", "70,000 TND", "65,000 TND", "Competitor B")
                ),
                onDismiss = { showPriceComparator = false }
            )
        }
    }
}

@Composable
fun ZaraPriceComparisonPopup(
    title: String,
    comparisonData: List<ProductComparison>,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            tonalElevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Comparison Data
                comparisonData.forEach { item ->
                    ProductComparisonRow(item)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Close Button
                Button(
                    onClick = { onDismiss() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Close", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ProductComparisonRow(item: ProductComparison) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = item.productName,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = "Zara: ${item.zaraPrice}",
            fontSize = 14.sp,
            color = Color.Gray
        )
        Text(
            text = "Lowest: ${item.competitorPrice}",
            fontSize = 14.sp,
            color = Color.Green
        )
        Text(
            text = "From: ${item.competitorName}",
            fontSize = 14.sp,
            color = Color.Blue
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

// Data class for product comparisons
data class ProductComparison(
    val productName: String,
    val zaraPrice: String,
    val competitorPrice: String,
    val competitorName: String
)

// Mock Zara Locations
private val zaraLocations = listOf(
    Brand(name = "Zara Mall of Tunis", latitude = 36.8314, longitude = 10.2414),
    Brand(name = "Zara City Centre", latitude = 36.8575, longitude = 10.2018)
)


