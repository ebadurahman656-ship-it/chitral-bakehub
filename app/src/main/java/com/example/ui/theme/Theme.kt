package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val BakeHubColorScheme = lightColorScheme(
    primary = BakeryOrange,
    onPrimary = PureWhite,
    primaryContainer = SoftBakeryBeige,
    onPrimaryContainer = ChocolateBrown,
    secondary = RoastedCoffee,
    onSecondary = PureWhite,
    secondaryContainer = SoftBakeryBeige,
    onSecondaryContainer = DeepBrownText,
    tertiary = GoldenHoney,
    onTertiary = ChocolateBrown,
    background = WarmCream,
    onBackground = DeepBrownText,
    surface = PureWhite,
    onSurface = DeepBrownText,
    surfaceVariant = SoftBakeryBeige,
    onSurfaceVariant = MutedBrownText,
    outline = WarmBeigeBorder,
    outlineVariant = WarmBeigeBorder,
    error = DarkRedError,
    onError = PureWhite,
    errorContainer = ErrorBg,
    onErrorContainer = DarkRedError
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = BakeHubColorScheme,
        typography = Typography,
        content = content
    )
}
