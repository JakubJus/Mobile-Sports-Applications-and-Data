package com.example.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.HourlyWeatherData
import com.example.weatherapp.data.WeatherData
import com.example.weatherapp.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val weatherRepository = WeatherRepository()

    private val _forecastList = MutableStateFlow<List<WeatherData>>(emptyList())
    val forecastList: StateFlow<List<WeatherData>> = _forecastList

    private val _hourlyForecastList = MutableStateFlow<List<HourlyWeatherData>>(emptyList())
    val hourlyForecastList: StateFlow<List<HourlyWeatherData>> = _hourlyForecastList


    fun fetchWeather(lat: Float, lon: Float) {
        viewModelScope.launch {
            val (dailyData, hourlyData) = weatherRepository.fetchWeather(lat, lon)
            _forecastList.value = dailyData
            _hourlyForecastList.value = hourlyData
        }
    }
}
