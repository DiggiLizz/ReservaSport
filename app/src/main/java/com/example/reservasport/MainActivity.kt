package com.example.reservasport

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.reservasport.ui.screens.ReservaSportScreen
import com.example.reservasport.ui.theme.ReservaSportTheme // <--- Importamos tu tema real

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Reemplazamos MaterialTheme genérico por tu tema personalizado
            ReservaSportTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.background
                ) {
                    ReservaSportScreen()
                }
            }
        }
    }
}