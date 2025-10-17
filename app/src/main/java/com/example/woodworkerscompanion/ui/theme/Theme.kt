package com.example.woodworkerscompanion.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = WoodPrimary,
    onPrimary = White,
    secondary = WoodSecondary,
    onSecondary = DarkBrown,
    tertiary = ForestGreen,
    onTertiary = White,
    background = CreamBackground,
    onBackground = DarkBrown,
    surface = White,
    onSurface = DarkBrown,
    surfaceVariant = WoodSecondary,
    onSurfaceVariant = DarkBrown,
    error = Color(0xFFB00020),
    onError = White
)

private val DarkColorScheme = darkColorScheme(
    primary = WoodSecondary,
    onPrimary = DarkBrown,
    secondary = WoodPrimary,
    onSecondary = White,
    tertiary = ForestGreen,
    onTertiary = White,
    background = DarkBrown,
    onBackground = CreamBackground,
    surface = Color(0xFF5D4E37),
    onSurface = CreamBackground,
    surfaceVariant = WoodPrimary,
    onSurfaceVariant = CreamBackground,
    error = Color(0xFFCF6679),
    onError = Black
)

@Composable
fun WoodworkersCompanionTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

