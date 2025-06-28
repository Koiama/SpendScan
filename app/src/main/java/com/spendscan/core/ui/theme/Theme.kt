package com.spendscan.core.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat


private val LightColorScheme = lightColorScheme(
    primary = GreenMain,
    onSurface = DarkGrey,
    onSecondary = GreenSecond,
    onTertiary = White,
    tertiary = GreyLight,
    background = Greyback,
    onSurfaceVariant = TextLightGrey,
    surfaceContainerHigh = SurfaceContainerHigh

)

@Composable
fun SpendScanTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars
            window.statusBarColor = LightColorScheme.primary.toArgb()
        }
    }

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}