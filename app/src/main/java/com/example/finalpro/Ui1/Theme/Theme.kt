package com.example.finalpro.Ui1.Theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun FinalProTheme(content: @Composable () -> Unit) {
    val colorScheme = lightColorScheme(
        primary           = GreenPrimary,
        secondary         = ColorInfo,
        background        = BgPrimary,
        surface           = BgSurface,
        surfaceVariant    = BgCardAlt,
        onPrimary         = Color.White,
        onSecondary       = Color.White,
        onBackground      = TextPrimary,
        onSurface         = TextPrimary,
        onSurfaceVariant  = TextSecondary,
        outline           = Border,
        error             = ColorGasto
    )
    MaterialTheme(
        colorScheme = colorScheme,
        typography  = FinanceTypography,
        content     = content
    )
}