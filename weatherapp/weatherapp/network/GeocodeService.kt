package com.example.weatherapp.network

import com.example.weatherapp.data.CityData
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodeService {
    @GET("search")
    suspend fun searchCities(
        @Query("q") query: String,
        @Query("api_key") apiKey: String = "672f48ba278ae907246358vxo1a6291"
    ): List<CityData>
}
