package com.example.horegify.ui.theme

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

private val LightColors = lightColorScheme(
    primary = Blue9,
    onPrimary = BlueContrast,
    primaryContainer = Blue6,
    onPrimaryContainer = Blue12,
    secondary = Gray8,
    onSecondary = GrayContrast,
    background = Gray1,
    onBackground = Gray12,
    surface = GraySurface,
    onSurface = Gray12,
    error = Color(0xFFB00020),
    onError = Color.White,
)

private val DarkColors = darkColorScheme(
    primary = DarkBlue9,
    onPrimary = DarkBlueContrast,
    primaryContainer = DarkBlue6,
    onPrimaryContainer = DarkBlue12,
    secondary = DarkGray8,
    onSecondary = DarkGrayContrast,
    background = DarkGray1,
    onBackground = DarkGray12,
    surface = DarkGraySurface,
    onSurface = DarkGray12,
    error = Color(0xFFCF6679),
    onError = Color.Black,
)


@Composable
fun HoregifyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            WindowCompat.setDecorFitsSystemWindows(window, false)
            window.statusBarColor = colors.primaryContainer.toArgb()
            val insetsController = WindowInsetsControllerCompat(window, window.decorView)
            insetsController.isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
//        content = {
//            val gradient = if (darkTheme) {
//                Brush.verticalGradient(
//                    colors = listOf(DarkBlue8, DarkBlue4)
//                )
//            } else {
//                Brush.verticalGradient(
//                    colors = listOf(Blue4, Blue9)
//                )
//            }
//
//            androidx.compose.foundation.layout.Box(
//                modifier = androidx.compose.ui.Modifier
//                    .background(brush = gradient)
//                    .fillMaxSize()
//            ) {
//                content()
//            }
//        }
    )
}
