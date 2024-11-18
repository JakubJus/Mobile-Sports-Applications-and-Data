package com.example.weatherapp.data

data class WeatherResponse(
    val daily: DailyData,
    val hourly: HourlyData
)

data class DailyData(
    val time: List<String>,
    val temperature_2m_max: List<Float>,
    val temperature_2m_min: List<Float>
)

data class HourlyData(
    val time: List<String>,
    val temperature_2m: List<Float>,
    val weather_code: List<Int>
)
