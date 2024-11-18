package com.example.weatherapp.repository

import com.example.weatherapp.data.HourlyWeatherData
import com.example.weatherapp.data.WeatherData
import com.example.weatherapp.network.WeatherService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(WeatherService::class.java)

    suspend fun fetchWeather(lat: Float, lon: Float): Pair<List<WeatherData>, List<HourlyWeatherData>> {
        val response = service.getWeather(lat, lon)

        // Map daily data
        val dailyData = response.daily.time.mapIndexed { index, date ->
            WeatherData(
                date = date,
                temperatureMax = response.daily.temperature_2m_max.getOrNull(index),
                temperatureMin = response.daily.temperature_2m_min.getOrNull(index)
            )
        }

        // Map hourly data
        val hourlyData = response.hourly.time.mapIndexed { index, time ->
            HourlyWeatherData(
                time = time,
                temperature = response.hourly.temperature_2m.getOrNull(index),
                weatherCode = response.hourly.weather_code.getOrNull(index)
            )
        }

        return Pair(dailyData, hourlyData)
    }
}
