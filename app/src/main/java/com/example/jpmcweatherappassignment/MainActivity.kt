package com.example.jpmcweatherappassignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.jpmcweatherappassignment.data.RetrofitInstance
import com.example.jpmcweatherappassignment.repository.WeatherRepository
import com.example.jpmcweatherappassignment.ui.screens.CombinedWeatherCard
import com.example.jpmcweatherappassignment.ui.screens.SearchBar
import com.example.jpmcweatherappassignment.ui.theme.JPMCWeatherAppAssignmentTheme
import com.example.jpmcweatherappassignment.viewmodel.WeatherViewModel
import com.example.jpmcweatherappassignment.viewmodel.WeatherViewModelFactory
import dagger.hilt.android.AndroidEntryPoint

class MainActivity : ComponentActivity() {

    /*
    Used simple UI to display the data
    Due to time constraint, I could not use Dagger2 or Hilt for dependency injection
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val api = RetrofitInstance.api
        val repository = WeatherRepository(api)
        val viewModel: WeatherViewModel

        // Initialize the repository

        // Create the ViewModel using the WeatherViewModelFactory
        val viewModelFactory = WeatherViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[WeatherViewModel::class.java]

        setContent {
            JPMCWeatherAppAssignmentTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),  // Add padding to the entire screen
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // The search bar at the top
                    SearchBar(viewModel = viewModel)

                    Spacer(modifier = Modifier.height(16.dp))

                    viewModel.weatherState.value?.let {
                        CombinedWeatherCard(weatherResponse = it)
                    } ?: run {
                        Text("Search for a city to see weather details")
                    }
                }
            }
        }
    }
}
