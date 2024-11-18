package com.example.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.CityData
import com.example.weatherapp.repository.CityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CityViewModel : ViewModel() {
    private val cityRepository = CityRepository()
    private val _searchResults = MutableStateFlow<List<CityData>>(emptyList())
    val searchResults: StateFlow<List<CityData>> = _searchResults

    fun searchCities(query: String) {
        viewModelScope.launch {
            _searchResults.value = cityRepository.searchCities(query)
        }
    }
}
