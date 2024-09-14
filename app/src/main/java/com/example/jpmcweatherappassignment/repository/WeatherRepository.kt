package com.example.jpmcweatherappassignment.repository

import com.example.jpmcweatherappassignment.data.WeatherApiService
import com.example.jpmcweatherappassignment.data.WeatherResponse
import javax.inject.Inject

class WeatherRepository(private val apiService: WeatherApiService) {
    suspend fun getWeather(cityName: String, appKey: String): WeatherResponse{
        return apiService.getWeather(cityName = cityName, appId = appKey)
    }
}