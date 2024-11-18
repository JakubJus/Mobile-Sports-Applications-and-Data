package com.example.weatherapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.viewmodel.FavoritesViewModel
import com.example.weatherapp.data.FavoriteCity

@Composable
fun HomeScreen(
    favoritesViewModel: FavoritesViewModel,
    onSearchClicked: () -> Unit,
    onCitySelected: (Float, Float) -> Unit
) {
    // Collect favoriteCities StateFlow within the @Composable function
    val favoriteCities by favoritesViewModel.favoriteCities.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {


        Button(onClick = onSearchClicked, modifier = Modifier.fillMaxWidth()) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Favorites", style = MaterialTheme.typography.titleMedium, fontSize = 20.sp)

        // Display the list of favorite cities
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(favoriteCities) { city ->
                TextButton(onClick = { onCitySelected(city.lat, city.lon) }) {
                    Text(
                        text = city.name,
                        color = MaterialTheme.colorScheme.onSecondary // Use onSecondary color from theme
                    )

                }
                Divider(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f))
            }
        }
    }
}
