package com.example.reservasport.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.reservasport.data.Cancha

@Composable
fun CardCancha(cancha: Cancha, onCardClick: () -> Unit) {
    val esReservada = cancha.disponibilidad == "RESERVADA por ti"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() },
        colors = CardDefaults.cardColors(
            // CORRECCIÓN: Si está reservada, usamos primaryContainer (un morado claro de fondo)
            containerColor = if (esReservada) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // Título de la cancha
                Text(
                    text = cancha.nombre,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (esReservada) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))

                // Disciplina deportiva
                Text(
                    text = "Disciplina: ${cancha.deporte}",
                    fontSize = 14.sp,
                    color = if (esReservada) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Estado de disponibilidad (Texto crítico de la reserva)
                Text(
                    text = cancha.disponibilidad,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    // CORRECCIÓN: Usamos onPrimaryContainer para que el texto sea un morado oscuro sobre el fondo claro
                    color = if (esReservada) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.primary
                )
            }

            // Indicador de Estado (Chip)
            SuggestionChip(
                onClick = { },
                label = {
                    Text(
                        text = if (esReservada) "✓ LISTO" else "LIBRE",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 12.sp
                    )
                },
                colors = SuggestionChipDefaults.suggestionChipColors(
                    // CORRECCIÓN: Contraste explícito para el chip interno
                    containerColor = if (esReservada) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                    labelColor = if (esReservada) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}