package com.sergioborne.nqueens.ui.theme

import androidx.compose.ui.graphics.Color

internal val LightColorPalette = NQueensColors(
    chessBoard1 = Color(196, 196, 196),
    chessBoard2 = Color.White,
    queenIcon = Color.Blue,
    attacked = Color.Red,
    container = Color(red = 234, green = 221, blue = 255),
    background = Color(red = 254, green = 247, blue = 255),
    textPrimary = Color.Black,
    textSecondary = Color.Gray,
    surfaceContainer = Color(red = 243, green = 237, blue = 247),
    onSurfaceContainer = Color(red = 29, green = 27, blue = 32),
    navigationBarItemColor = Color.Gray,
    navigationBarSelectedItemColor = Color.Blue,
    navigationBarIndicatorColor = Color(red = 232, green = 222, blue = 248),
)

internal val DarkColorPalette = NQueensColors(
    chessBoard1 = Color.Black,
    chessBoard2 = Color(117, 116, 116),
    queenIcon = Color.Blue,
    attacked = Color.Red,
    container = Color(red = 79, green = 55, blue = 139),
    background = Color(red = 20, green = 18, blue = 24),
    textPrimary = Color.White,
    textSecondary = Color.LightGray,
    surfaceContainer = Color(red = 33, green = 31, blue = 38),
    onSurfaceContainer = Color(red = 230, green = 224, blue = 233),
    navigationBarItemColor = Color.LightGray,
    navigationBarSelectedItemColor = Color(red = 168, green = 255, blue = 255),
    navigationBarIndicatorColor = Color(red = 74, green = 68, blue = 88),
)