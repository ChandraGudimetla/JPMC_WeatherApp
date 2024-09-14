package com.example.jpmcweatherappassignment.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jpmcweatherappassignment.data.WeatherResponse
import com.example.jpmcweatherappassignment.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class WeatherViewModel (private val repository: WeatherRepository) : ViewModel() {

    private val _weatherState = mutableStateOf<WeatherResponse?>(null)
    val weatherState: State<WeatherResponse?> = _weatherState

    var cityName by mutableStateOf("")

    fun fetchWeather(apiKey: String) {
        if (cityName.isBlank()) {
            return
        }
        viewModelScope.launch {
            try {
                val response = repository.getWeather(cityName, apiKey)
                _weatherState.value = response
            } catch (e: Exception) {
                println(e.stackTrace)
            }
        }
    }

}