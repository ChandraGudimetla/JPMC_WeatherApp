package com.example.jpmcweatherappassignment.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.jpmcweatherappassignment.viewmodel.WeatherViewModel

@Composable
fun LocationPermissionUI(viewModel: WeatherViewModel) {
    var locationPermissionGranted by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Location permission launcher
    val requestPermissionLocationLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            locationPermissionGranted = isGranted
            Log.i("Chandra", "Permission granted: $locationPermissionGranted")
        }

    // Request permission on launch
    LaunchedEffect(Unit) {
        val permissionStatus = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLocationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            locationPermissionGranted = true
            Log.i("Chandra", "Permission already granted.")
        }
    }

    // Observing weatherState
    val weatherData by viewModel.weatherState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        if (locationPermissionGranted) {
            SearchBar(viewModel = viewModel)

            Spacer(modifier = Modifier.height(16.dp))

            // Display the weather data if available
            weatherData?.let {
                Log.i("Chandra", "Weather Data: ${it.name}")
                CombinedWeatherCard(weatherResponse = it)
            }?:run { 
                Text(text = "Search for a city to see weather details")
            }
        } else {
            Text("Location permission is required to use the app.")
        }
    }
}

