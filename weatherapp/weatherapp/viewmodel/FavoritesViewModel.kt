package com.example.weatherapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.DataStoreManager
import com.example.weatherapp.data.FavoriteCity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStoreManager = DataStoreManager(application)
    private val _favoriteCities = MutableStateFlow<List<FavoriteCity>>(emptyList())
    val favoriteCities: StateFlow<List<FavoriteCity>> = _favoriteCities

    init {
        loadFavoriteCities()
    }
    private fun loadFavoriteCities() {
        viewModelScope.launch {
            dataStoreManager.getFavoriteCities().collectLatest { cities ->
                _favoriteCities.value = cities.distinctBy { it.name }
            }
        }
    }

    fun addFavorite(city: FavoriteCity) {
        viewModelScope.launch {
            if (_favoriteCities.value.none { it.name == city.name }) {
                dataStoreManager.addFavoriteCity(city.name, city.lat, city.lon)
                loadFavoriteCities()
            }
        }
    }

    // Remove a city from favorites
    fun removeFavorite(cityName: String) {
        viewModelScope.launch {
            dataStoreManager.removeFavoriteCity(cityName)
            loadFavoriteCities() // Refresh the list
        }
    }

    // Check if a city is in favorites
    fun isFavorite(cityName: String): Boolean {
        return _favoriteCities.value.any { it.name == cityName }
    }
}
