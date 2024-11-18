package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import com.example.weatherapp.ui.*
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.viewmodel.*

class MainActivity : ComponentActivity() {
    private val cityViewModel: CityViewModel by viewModels()
    private val weatherViewModel: WeatherViewModel by viewModels()
    private val favoritesViewModel: FavoritesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {

                var currentScreen by remember { mutableStateOf("Home") }
                var selectedCityName by remember { mutableStateOf("") }
                var lat by remember { mutableStateOf(0f) }
                var lon by remember { mutableStateOf(0f) }

                Surface {
                    when (currentScreen) {
                        "Home" -> HomeScreen(
                            favoritesViewModel = favoritesViewModel,
                            onSearchClicked = { currentScreen = "Search" },
                            onCitySelected = { selectedLat, selectedLon ->
                                selectedCityName =
                                    favoritesViewModel.favoriteCities.value.find { it.lat == selectedLat && it.lon == selectedLon }?.name
                                        ?: ""
                                lat = selectedLat
                                lon = selectedLon
                                currentScreen = "Weather"
                            }
                        )

                        "Search" -> SearchScreen(
                            viewModel = cityViewModel,
                            onCitySelected = { selectedLat, selectedLon, name ->
                                selectedCityName =
                                    name  // Set the city name when selected from SearchScreen
                                lat = selectedLat
                                lon = selectedLon
                                currentScreen = "Weather"
                            }
                        )

                        "Weather" -> WeatherScreen(
                            viewModel = weatherViewModel,
                            favoritesViewModel = favoritesViewModel,
                            cityName = selectedCityName,  // Pass the city name to WeatherScreen
                            lat = lat,
                            lon = lon,
                            onBackToHome = { currentScreen = "Home" }
                        )
                    }
                }
            }
        }
    }
}
