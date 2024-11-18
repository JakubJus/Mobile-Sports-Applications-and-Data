package com.example.weatherapp.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Define shapes with custom corner radii
val WeatherAppShapes = Shapes(
    small = RoundedCornerShape(4.dp),   // Small elements like buttons
    medium = RoundedCornerShape(8.dp),  // Medium elements like cards
    large = RoundedCornerShape(16.dp)   // Large elements like bottom sheets
)
