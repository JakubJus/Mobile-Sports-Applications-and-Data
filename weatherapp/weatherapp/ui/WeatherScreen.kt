package com.example.weatherapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.Umbrella
import androidx.compose.material.icons.filled.WbCloudy
import androidx.compose.material.icons.filled.WbSunny
import com.example.weatherapp.viewmodel.WeatherViewModel
import com.example.weatherapp.viewmodel.FavoritesViewModel
import com.example.weatherapp.data.FavoriteCity

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel,
    favoritesViewModel: FavoritesViewModel,
    cityName: String,
    lat: Float,
    lon: Float,
    onBackToHome: () -> Unit
) {
    println("WeatherScreen cityName: $cityName")
    LaunchedEffect(Unit) {
        viewModel.fetchWeather(lat, lon)
    }

    val forecastList by viewModel.forecastList.collectAsState()
    val hourlyForecastList by viewModel.hourlyForecastList.collectAsState()

    // Track whether the city is a favorite
    var isFavorite by remember { mutableStateOf(favoritesViewModel.isFavorite(cityName)) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = {
                    if (isFavorite) {
                        favoritesViewModel.removeFavorite(cityName)
                    } else {
                        favoritesViewModel.addFavorite(FavoriteCity(cityName, lat, lon))
                    }
                    isFavorite = !isFavorite
                }) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Favorite Icon",
                        tint = if (isFavorite) Color.Yellow else Color.Gray,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Text(
                    text = cityName,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Hourly Forecast Section
            Text("Hourly Forecast", style = MaterialTheme.typography.titleMedium, fontSize = 20.sp)
            LazyRow {
                items(hourlyForecastList) { hourly ->
                    val hour = hourly.time?.substringAfter("T") ?: "N/A"
                    val temperature = hourly.temperature ?: 0f

                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                            .width(80.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = hour)
                        Icon(
                            imageVector = getWeatherIcon(hourly.weatherCode),
                            contentDescription = "Weather Icon",
                            tint = Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "$temperature°C")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Weekly Forecast", style = MaterialTheme.typography.titleMedium, fontSize = 20.sp)
            LazyRow {
                items(forecastList) { weather ->
                    val day = weather.date?.substringAfterLast("-") ?: "N/A"
                    val temperatureMax = weather.temperatureMax ?: 0f
                    val temperatureMin = weather.temperatureMin ?: 0f

                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                            .width(100.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(day)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text("Max: $temperatureMax°C")
                        Text("Min: $temperatureMin°C")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(
            onClick = onBackToHome,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Back to Home")
        }
    }
}

@Composable
fun getWeatherIcon(weatherCode: Int?): androidx.compose.ui.graphics.vector.ImageVector {
    return when (weatherCode) {
        0 -> Icons.Filled.WbSunny                   // Sunny
        1, 2, 3 -> Icons.Filled.WbCloudy            // Partly cloudy
        45, 48 -> Icons.Filled.Cloud                // Cloudy
        80, 81, 82 -> Icons.Filled.Umbrella         // Showers
        95, 96, 99 -> Icons.Filled.Thunderstorm     // Thunderstorm
        71, 73, 75, 77 -> Icons.Filled.AcUnit       // Snow
        else -> Icons.Filled.WbSunny                // Default to sunny
    }
}