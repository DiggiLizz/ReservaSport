package com.example.reservasport.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.reservasport.data.Cancha
import com.example.reservasport.data.PreferenciasReserva
import com.example.reservasport.ui.components.CardCancha
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservaSportScreen() {
    val context = LocalContext.current
    val prefsManager = remember { PreferenciasReserva(context) }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    // CORRECCIÓN CRÍTICA: Llamamos a 'obtenerHorarioOriginal' desde la misma carga inicial de datos
    val listaCanchas = remember {
        mutableStateListOf(
            Cancha(
                id = 1,
                nombre = "Complejo Deportivo San Miguel",
                deporte = "Fútbol 7",
                disponibilidad = prefsManager.obtenerEstadoCancha(1, "Disponible: ${obtenerHorarioOriginal(1)}")
            ),
            Cancha(
                id = 2,
                nombre = "Club de Tenis El Llano",
                deporte = "Tenis",
                disponibilidad = prefsManager.obtenerEstadoCancha(2, "Disponible: ${obtenerHorarioOriginal(2)}")
            ),
            Cancha(
                id = 3,
                nombre = "Gimnasio Municipal Urbano",
                deporte = "Básquetbol",
                disponibilidad = prefsManager.obtenerEstadoCancha(3, "Disponible: ${obtenerHorarioOriginal(3)}")
            ),
            // Nuevas canchas para habilitar el scroll dinámico en la entrega:
            Cancha(
                id = 4,
                nombre = "Estadio Recreativo Gran Avenida",
                deporte = "Fútbol 7",
                disponibilidad = prefsManager.obtenerEstadoCancha(4, "Disponible: ${obtenerHorarioOriginal(4)}")
            ),
            Cancha(
                id = 5,
                nombre = "Pádel Arena San Miguel",
                deporte = "Tenis",
                disponibilidad = prefsManager.obtenerEstadoCancha(5, "Disponible: ${obtenerHorarioOriginal(5)}")
            ),
            Cancha(
                id = 6,
                nombre = "Polideportivo Central Urbano",
                deporte = "Básquetbol",
                disponibilidad = prefsManager.obtenerEstadoCancha(6, "Disponible: ${obtenerHorarioOriginal(6)}")
            )
        )
    }

    var canchaSeleccionada by remember { mutableStateOf<Cancha?>(null) }
    var deporteSeleccionado by remember { mutableStateOf("Todos") }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Column {
                        Text("ReservaSport", fontWeight = FontWeight.ExtraBold)
                        Text("Gestión de espacios comunitarios", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Filtros superiores estilizados
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Todos", "Fútbol 7", "Tenis", "Básquetbol").forEach { deporte ->
                    FilterChip(
                        selected = deporteSeleccionado == deporte,
                        onClick = { deporteSeleccionado = deporte },
                        label = { Text(deporte) }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val listaFiltrada = if (deporteSeleccionado == "Todos") {
                    listaCanchas
                } else {
                    listaCanchas.filter { it.deporte == deporteSeleccionado }
                }

                items(listaFiltrada, key = { it.id }) { cancha ->
                    CardCancha(cancha = cancha, onCardClick = { canchaSeleccionada = cancha })
                }
            }
        }

        canchaSeleccionada?.let { cancha ->
            val yaEstaReservada = cancha.disponibilidad == "RESERVADA por ti"

            AlertDialog(
                onDismissRequest = { canchaSeleccionada = null },
                confirmButton = {
                    Button(
                        onClick = {
                            val index = listaCanchas.indexOfFirst { it.id == cancha.id }
                            if (index != -1) {
                                // Si ya estaba reservada, restauramos el texto original. Si no, la reservamos.
                                val nuevoEstado = if (yaEstaReservada) {
                                    "Disponible: ${obtenerHorarioOriginal(cancha.id)}"
                                } else {
                                    "RESERVADA por ti"
                                }

                                // 1. Persistencia física local en SharedPreferences
                                prefsManager.guardarEstadoCancha(cancha.id, nuevoEstado)

                                // 2. Actualización reactiva de la UI
                                listaCanchas[index] = listaCanchas[index].copy(disponibilidad = nuevoEstado)
                            }
                            canchaSeleccionada = null
                        },
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Text(if (yaEstaReservada) "Liberar Cancha" else "Confirmar Reserva")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { canchaSeleccionada = null }) {
                        Text("Cerrar")
                    }
                },
                title = {
                    Text(
                        text = if (yaEstaReservada) "Gestionar Reserva" else "Confirmar Reserva",
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = if (yaEstaReservada) {
                            "Actualmente tienes una reserva en '${cancha.nombre}'. ¿Deseas liberar este espacio para la comunidad?"
                        } else {
                            "¿Deseas registrar una reserva en '${cancha.nombre}' para la disciplina de ${cancha.deporte}?"
                        }
                    )
                }
            )
        }
    }
}

// LA FUNCIÓN AUXILIAR ABAJO: Ahora sí está perfectamente amarrada con la UI de arriba
fun obtenerHorarioOriginal(id: Int): String {
    return when (id) {
        1 -> "16:00 - 18:00"
        2 -> "14:00 - 15:30"
        3 -> "19:00 - 21:00"
        else -> "10:00 - 12:00"
    }
}

// Agrega esta línea justo arriba de la previsualización:
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ReservaSportPreview() {
    MaterialTheme {
        Scaffold(
            topBar = {
                LargeTopAppBar(
                    title = {
                        Column {
                            Text("ReservaSport", fontWeight = FontWeight.ExtraBold)
                            Text("Gestión de espacios comunitarios (Vista Previa)", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                )
            }
        ) { paddingValues ->
            // ... (el resto del código del preview queda exactamente igual)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(selected = true, onClick = {}, label = { Text("Todos") })
                    FilterChip(selected = false, onClick = {}, label = { Text("Fútbol 7") })
                    FilterChip(selected = false, onClick = {}, label = { Text("Tenis") })
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = "Complejo San Miguel (Preview)", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                Text(text = "Disciplina: Fútbol 7", fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "Disponible: 16:00 - 18:00", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            }
                            SuggestionChip(onClick = {}, label = { Text("LIBRE", fontWeight = FontWeight.ExtraBold) })
                        }
                    }
                }
            }
        }
    }
}