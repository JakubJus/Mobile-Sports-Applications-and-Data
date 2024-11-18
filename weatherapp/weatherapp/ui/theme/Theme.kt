package com.example.weatherapp.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryColor,
    onPrimary = OnPrimaryColor,
    secondary = SecondaryColor,
    onSecondary = OnSecondaryColor,
    background = BackgroundColor,
    onBackground = OnBackgroundColor,
    surface = SurfaceColor,
    onSurface = OnSurfaceColor,
    error = ErrorColor,
    onError = Color.White // white text/icons on error color
)

@Composable
fun WeatherAppTheme(
    darkTheme: Boolean = true, // Default to dark theme
    content: @Composable () -> Unit
) {
    // Apply the dark color scheme and custom shapes
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        shapes = WeatherAppShapes, // Use the custom shapes here
        content = content
    )
}
