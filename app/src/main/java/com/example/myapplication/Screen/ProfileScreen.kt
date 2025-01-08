package com.example.myapplication.Screen
import okhttp3.OkHttpClient
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.myapplication.Activity.*
import com.example.myapplication.R
import com.example.myapplication.ViewModel.UserViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
import com.example.myapplication.Model.PriceComparison
import org.json.JSONArray

@Composable
fun ProfileScreen(viewModel: UserViewModel) {
    val user = viewModel.currentUser.observeAsState()
    val context = LocalContext.current
    val userData = user.value
    var showMapDialog by remember { mutableStateOf(false) }

    // The state for showing product comparison dialog
    var selectedBrand by remember { mutableStateOf<PriceComparison?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7EEE2))
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_back),
            contentDescription = "Back",
            tint = Color(0xFF5D5C56),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .size(24.dp)
                .clickable {
                    val intent = Intent(context, HomeScreenActivity::class.java)
                    context.startActivity(intent)
                }
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_edit),
            contentDescription = "Edit Profile",
            tint = Color(0xFF5D5C56),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(24.dp)
                .clickable {
                    userData?.let {
                        val intent = Intent(context, EditAccountActivity::class.java)
                        intent.putExtra("userData", it)
                        context.startActivity(intent)
                    }
                }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            if (user.value != null) {
                val userData = user.value!!
                // Profile Image
                val url = "http://192.168.48.172:3000/uploads/${userData.imageProfile}"
                AsyncImage(
                    model = url,
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFAA8F5C)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                // User Info
                Text(
                    text = "${userData.nom} ${userData.prenom}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5D5C56)
                )
                Text(
                    text = userData.email,
                    fontSize = 16.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(60.dp))

                // Functional Buttons
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    ProfileButton("Your Orders", Icons.Default.ShoppingCart, Color(0xFF5D5C56)) {
                        context.startActivity(Intent(context, CartActivity::class.java))
                    }

                    ProfileButton("Your Favourites", Icons.Default.Favorite, Color(0xFF5D5C56)) {}

                    ProfileButton("Payment", Icons.Default.Favorite, Color(0xFF5D5C56)) {}

                    ProfileButton("Recommended Shops", Icons.Default.Place, Color(0xFF5D5C56)) {}

                    ProfileButton("Nearest Shop", Icons.Default.LocationOn, Color(0xFF5D5C56)) {
                        showMapDialog = true // Show map dialog when clicked
                    }

                    // Show Map Dialog when button is clicked
                    if (showMapDialog) {
                        MapDialog(
                            onDismiss = { showMapDialog = false },
                            onLocationSelected = { selectedLocation ->
                                Log.e("TAG", "Selected location: $selectedLocation")
                                showMapDialog = false
                            }
                        )
                    }

                    ProfileButton("Settings", Icons.Default.Settings, Color(0xFF5D5C56)) {}
                }

                Spacer(modifier = Modifier.weight(1f))

                // Logout Button
                Button(
                    onClick = {
                        logoutAndNavigateToLogin(context)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color(0xFFAA8F5C)
                    ),
                    shape = RoundedCornerShape(50.dp),
                    border = BorderStroke(1.dp, Color(0xFFAA8F5C))
                ) {
                    Text(text = "Log Out", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            } else {
                CircularProgressIndicator(color = Color(0xFFAA8F5C))
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Loading user data...",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
            }
        }
    }

    // Show PriceComparisonDialog when a brand is selected
    selectedBrand?.let { comparison ->
        PriceComparisonDialog(comparison = comparison, onDismiss = {
            selectedBrand = null
        })
    }
}
@Composable
fun PriceComparisonDialog(comparison: PriceComparison, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            shape = MaterialTheme.shapes.medium,
            color = Color.White,
            tonalElevation = 4.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = comparison.productName, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Zara Price: ${comparison.zaraProductPrice}", fontSize = 18.sp)
                Text(text = "Competitor Price (${comparison.competitorName}): ${comparison.competitorPrice}", fontSize = 18.sp)
                Text(text = "Your Price: ${comparison.userProductPrice}", fontSize = 18.sp)

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    AsyncImage(
                        model = comparison.userProductImage,
                        contentDescription = "User Product Image",
                        modifier = Modifier.size(100.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    AsyncImage(
                        model = comparison.zaraProductImage,
                        contentDescription = "Zara Product Image",
                        modifier = Modifier.size(100.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onDismiss() },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color(0xFFAA8F5C)
                    )
                ) {
                    Text(text = "Close", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ProfileButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = color
        )
    }
}

fun logoutAndNavigateToLogin(context: Context) {
    val sharedPreferences = context.getSharedPreferences("shop_mate_prefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().clear().apply()

    val intent = Intent(context, LoginActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    context.startActivity(intent)
}

@Composable
fun MapDialog(onDismiss: () -> Unit, onLocationSelected: (String) -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            shape = MaterialTheme.shapes.medium,
            color = Color.White,
            tonalElevation = 4.dp
        ) {
            MapWithTextField(onLocationSelected)
        }
    }
}

@Composable
fun MapWithTextField(onLocationSelected: (String) -> Unit) {
    val context = LocalContext.current
    val fusedLocationClient = com.google.android.gms.location.LocationServices.getFusedLocationProviderClient(context)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(LatLng(36.8065, 10.1815), 12f, 0f, 0f) // Par défaut, Tunis
    }

    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }

    // Lancer la demande de permission
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                fetchUserLocation(fusedLocationClient, cameraPositionState) { location ->
                    userLocation = location
                }
            } else {
                Log.e("MapWithTextField", "Location permission denied")
            }
        }
    )

    LaunchedEffect(Unit) {
        when {
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
                fetchUserLocation(fusedLocationClient, cameraPositionState) { location ->
                    userLocation = location
                }
            }
            else -> permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    GoogleMap(
        cameraPositionState = cameraPositionState,
        modifier = Modifier.fillMaxSize(),
        onMapClick = { latLng ->
            selectedLocation = latLng
            onLocationSelected("Selected Location: ${latLng.latitude}, ${latLng.longitude}")
        }
    ) {
        // Marqueur pour la localisation de l'utilisateur
        userLocation?.let {
            Marker(
                state = rememberMarkerState(position = it),
                title = "Votre position"
            )
        }

        // Marqueurs pour les magasins Zara à Tunis
        zaraLocations.forEach { zara ->
            Marker(
                state = rememberMarkerState(position = LatLng(zara.latitude, zara.longitude)),
                title = zara.name,
                onClick = {
                    // Navigate to BrandComparisonActivity
                    val intent = Intent(context, BrandComparisonActivity::class.java).apply {
                        putExtra("brandName", zara.name)
                        putExtra("latitude", zara.latitude)
                        putExtra("longitude", zara.longitude)
                    }
                    context.startActivity(intent)
                    true // Return true to indicate the click has been handled
                }
            )
        }

        // Marqueur pour la localisation sélectionnée
        selectedLocation?.let {
            Marker(
                state = rememberMarkerState(position = it),
                title = "Localisation sélectionnée"
            )
        }
    }
}
private val zaraLocations = listOf(
    Brand(name = "Zara Mall of Tunis", latitude = 36.8314, longitude = 10.2414),
    Brand(name = "Zara City Centre", latitude = 36.8575, longitude = 10.2018)
)



// Helper function to fetch user location
@SuppressLint("MissingPermission")
private fun fetchUserLocation(
    fusedLocationClient: com.google.android.gms.location.FusedLocationProviderClient,
    cameraPositionState: CameraPositionState,
    onLocationFetched: (LatLng) -> Unit
) {
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        if (location != null) {
            val latLng = LatLng(location.latitude, location.longitude)
            cameraPositionState.position = CameraPosition(latLng, 15f, 0f, 0f)
            onLocationFetched(latLng)
        } else {
            Log.e("fetchUserLocation", "Unable to get location")
        }
    }.addOnFailureListener {
        Log.e("fetchUserLocation", "Failed to get location", it)
    }
}

// Helper function to fetch nearby brands from the backend
private val client = OkHttpClient()

private fun fetchNearbyBrands(lat: Double, lng: Double, onResult: (List<Brand>) -> Unit) {
    // Mock data for nearby brands, this can be fetched from the backend later
    val mockBrands = listOf(
        Brand(name = "Mock Brand 1", latitude = lat + 0.01, longitude = lng + 0.01),
        Brand(name = "Mock Brand 2", latitude = lat - 0.01, longitude = lng - 0.01)
    )
    onResult(mockBrands)
}



//private fun parseBrands(responseBody: String?): List<Brand> {
//    return try {
//        val jsonArray = JSONArray(responseBody)
//        (0 until jsonArray.length()).map { i ->
//            val jsonObject = jsonArray.getJSONObject(i)
//            Brand(
//                name = jsonObject.getString("name"),
//                latitude = jsonObject.getDouble("latitude"),
//                longitude = jsonObject.getDouble("longitude")
//            )
//        }
//    } catch (e: Exception) {
//        Log.e("parseBrands", "Error parsing brands", e)
//        emptyList()
//    }
//}

// Data class for brands
data class Brand(
    val name: String,
    val latitude: Double,
    val longitude: Double
)
