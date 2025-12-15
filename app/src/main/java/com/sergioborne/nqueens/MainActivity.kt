package com.sergioborne.nqueens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.sergioborne.nqueens.ui.navigation.AppNavigation
import com.sergioborne.nqueens.ui.theme.NQueensTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NQueensTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = NQueensTheme.colors.background,
                ) {
                    AppNavigation()
                }
            }
        }
    }
}
