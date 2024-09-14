package com.example.jpmcweatherappassignment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.jpmcweatherappassignment.data.Clouds
import com.example.jpmcweatherappassignment.data.Coord
import com.example.jpmcweatherappassignment.data.Main
import com.example.jpmcweatherappassignment.data.Sys
import com.example.jpmcweatherappassignment.data.Weather
import com.example.jpmcweatherappassignment.data.WeatherResponse
import com.example.jpmcweatherappassignment.data.Wind
import com.example.jpmcweatherappassignment.repository.WeatherRepository
import com.example.jpmcweatherappassignment.viewmodel.WeatherViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: WeatherRepository

    private lateinit var viewModel: WeatherViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = WeatherViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchWeather should update weatherState with data from repository`() = runTest {
        val mockResponse = WeatherResponse(
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

        // Mock the repository to return the mock response for any city name
        whenever(repository.getWeather(anyString(), anyString()))
            .thenReturn(mockResponse)

        viewModel.cityName = "Chicago"

        viewModel.fetchWeather("b4fea2e6b52af8503cd3f2735e44911e")

        verify(repository).getWeather("Chicago", "b4fea2e6b52af8503cd3f2735e44911e")

        assert(viewModel.weatherState.value == mockResponse)
    }
}
