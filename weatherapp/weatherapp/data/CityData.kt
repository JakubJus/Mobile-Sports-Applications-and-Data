package com.example.weatherapp.data

import com.google.gson.annotations.SerializedName

data class CityData(
    @SerializedName("display_name") val displayName: String?,
    @SerializedName("lat") val lat: Float?,
    @SerializedName("lon") val lon: Float?
)
