package com.example.jpmcweatherappassignment.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    //https://api.openweathermap.org/data/2.5/weather?q=Miami,FL,US&appid=b4fea2e6b52af8503cd3f2735e44911e&units=metric
    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("q") cityName: String,
        @Query("appid") appId: String,
        @Query("units") units: String = "metric"
    ): WeatherResponse
}

// Singleton object, so there can be just a single instance through out the app
/*
Commenting this code since we are using hilt and going forward Hilt will take care
of creating the instances of the retrofit and providing the api service


 */

object RetrofitInstance {
    private const val BASE_URL = "https://api.openweathermap.org/"

    val api: WeatherApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    }
}
