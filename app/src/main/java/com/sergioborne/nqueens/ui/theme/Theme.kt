package com.sergioborne.nqueens.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Composable
fun NQueensTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    CompositionLocalProvider(LocalNQueensColors provides colors) {
        MaterialTheme(
            content = content
        )
    }
}

object NQueensTheme {
    val colors: NQueensColors
        @Composable
        @ReadOnlyComposable
        get() = LocalNQueensColors.current
}

@Immutable
data class NQueensColors(
    val chessBoard1: Color,
    val chessBoard2: Color,
    val queenIcon: Color,
    val attacked: Color,
    val container: Color,
    val background: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val surfaceContainer: Color,
    val onSurfaceContainer: Color,
    val navigationBarItemColor: Color,
    val navigationBarSelectedItemColor: Color,
    val navigationBarIndicatorColor: Color,
)

private val LocalNQueensColors = staticCompositionLocalOf<NQueensColors> {
    error("No NQueens color palette provided")
}
