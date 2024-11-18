package com.example.weatherapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weatherapp.viewmodel.CityViewModel

@Composable
fun SearchScreen(
    viewModel: CityViewModel,
    onCitySelected: (Float, Float, String) -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    val searchResults by viewModel.searchResults.collectAsState()

    // Use MaterialTheme's color scheme for background color
    val backgroundColor = MaterialTheme.colorScheme.surface

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor) // Use theme background color
            .padding(16.dp)
    ) {
        TextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Enter city name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        Button(
            onClick = { viewModel.searchCities(searchText) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor) // Ensure the list background is consistent
        ) {
            items(searchResults) { city ->
                TextButton(onClick = { onCitySelected(city.lat ?: 0f, city.lon ?: 0f, city.displayName ?: "") }) {
                    Text(
                        city.displayName ?: "Unknown Place",
                        color = MaterialTheme.colorScheme.onBackground // Text color based on theme
                    )
                }
                Divider(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)) // Optional divider
            }
        }
    }
}
