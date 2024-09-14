package com.example.jpmcweatherappassignment

import com.example.jpmcweatherappassignment.data.*
import com.example.jpmcweatherappassignment.repository.WeatherRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class WeatherRepositoryTest {

    @Mock
    private lateinit var weatherApiService: WeatherApiService

    private lateinit var weatherRepository: WeatherRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        weatherRepository = WeatherRepository(weatherApiService)
    }

    @Test
    fun `test getWeather fetches weather from API`() = runBlocking {
        val cityName = "Chicago"
        val apiKey = "b4fea2e6b52af8503cd3f2735e44911e"
        val weatherResponse = WeatherResponse(
            coord = Coord(lon = -122.4194, lat = 37.7749),
            weather = listOf(
                Weather(
                    id = 800,
                    main = "Clear",
                    description = "clear sky",
                    icon = "01d"
                )
            ),
            base = "stations",
            main = Main(
                temp = 21.33,
                feels_like = 21.42,
                temp_min = 19.51,
                temp_max = 22.24,
                pressure = 1020,
                humidity = 73,
                sea_level = 1020,
                grnd_level = 1005
            ),
            visibility = 10000,
            wind = Wind(
                speed = 2.57,
                deg = 150.0
            ),
            sys = Sys(
                type = 1,
                id = 1234,
                country = "US",
                sunrise = 1630504530,
                sunset = 1630551330
            ),
            clouds = Clouds(all = 75),
            name = "Chicago"
        )

        Mockito.`when`(weatherApiService.getWeather(cityName, apiKey, "metric"))
            .thenReturn(weatherResponse)

        val result = weatherRepository.getWeather(cityName, apiKey)

        Mockito.verify(weatherApiService).getWeather(cityName, apiKey, "metric")

        assert(result == weatherResponse)
    }
}
