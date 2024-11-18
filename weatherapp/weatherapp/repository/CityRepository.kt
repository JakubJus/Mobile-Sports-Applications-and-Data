package com.example.weatherapp.repository

import com.example.weatherapp.data.CityData
import com.example.weatherapp.network.GeocodeService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CityRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://geocode.maps.co/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(GeocodeService::class.java)

    suspend fun searchCities(query: String): List<CityData> {
        return service.searchCities(query)
    }
}
