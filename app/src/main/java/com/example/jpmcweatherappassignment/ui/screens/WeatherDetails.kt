package com.example.jpmcweatherappassignment.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.jpmcweatherappassignment.data.WeatherResponse
import com.example.jpmcweatherappassignment.viewmodel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SearchBar(viewModel: WeatherViewModel) {
    var cityName by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = cityName,
            onValueChange = {
                cityName = it
                viewModel.cityName = it
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search"
                )
            },
            modifier = Modifier.fillMaxWidth().background(Color.Black),
            label = { Text(text = "Enter city name") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.fetchWeather("b4fea2e6b52af8503cd3f2735e44911e")
                keyboardController?.hide()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Search")
        }
    }
}


@Composable
fun CombinedWeatherCard(weatherResponse: WeatherResponse) {
    Log.d("Chandra", "Inside CombinedWeatherCard")
    val iconUrl = "http://openweathermap.org/img/wn/${weatherResponse.weather.first().icon}@2x.png"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${weatherResponse.name}, ${weatherResponse.sys.country}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Weather Icon
            AsyncImage(
                model = iconUrl,
                contentDescription = "Weather Icon",
                modifier = Modifier.size(64.dp)
            )

            // Current temperature
            Text(
                text = "${weatherResponse.main.temp}°",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = weatherResponse.weather.first().description.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "H: ${weatherResponse.main.temp_max}°  L: ${weatherResponse.main.temp_min}°",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Feels Like: ${weatherResponse.main.feels_like}°")
                    Text("Humidity: ${weatherResponse.main.humidity}%")
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Wind: ${weatherResponse.wind.speed} KM")
                    Text("Pressure: ${weatherResponse.main.pressure} hPa")
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cloudiness: ${weatherResponse.clouds.all}%")
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val sunriseTime = formatTime(weatherResponse.sys.sunrise)
                    val sunsetTime = formatTime(weatherResponse.sys.sunset)
                    Text("Sunrise: $sunriseTime")
                    Text("Sunset: $sunsetTime")
                }
            }
        }
    }
}

fun formatTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return sdf.format(Date(timestamp * 1000)) // Convert from seconds to milliseconds
}
