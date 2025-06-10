package com.trackway.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Color(0xFF81C784),
    primaryVariant = Color(0xFF519657),
    secondary = Color(0xFF03DAC6),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    error = Color(0xFFCF6679)
)

private val LightColorPalette = lightColors(
    primary = Color(0xFF4CAF50),
    primaryVariant = Color(0xFF087F23),
    secondary = Color(0xFF03DAC6),
    background = Color.White,
    surface = Color(0xFFF5F5F5),
    error = Color(0xFFB00020)
)

@Composable
fun TrackWayTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        content = content
    )
} 