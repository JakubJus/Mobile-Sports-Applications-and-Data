package com.example.weatherapp.network

import com.example.weatherapp.data.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("v1/forecast")
    suspend fun getWeather(
        @Query("latitude") lat: Float,
        @Query("longitude") lon: Float,
        @Query("daily") daily: String = "temperature_2m_max,temperature_2m_min",
        @Query("forecast_hours") forecastHours: Int = 24,
        @Query("hourly") hourly: String = "temperature_2m,weather_code",
        @Query("timezone") timezone: String = "GMT"
    ): WeatherResponse
}
