package com.spendscan.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = SpringGreen,
    secondary = LightGreen,
    tertiary = Rhino,
    surface = WhiteLilac,
    onSurface = RaisinBlack,
    onPrimary = RaisinBlack,
    onSurfaceVariant = DarkGray,
    primaryContainer = DarkMagnolia,
    secondaryContainer = LightGreen,
    onSecondaryContainer = SpringGreen,
    outlineVariant = LavenderGray,
    surfaceContainerHigh = DarkLilac
)

@Composable
fun SpendScanTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
