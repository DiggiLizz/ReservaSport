package com.example.reservasport.data

import android.content.Context
import android.content.SharedPreferences

class PreferenciasReserva(context: Context) {
    // Inicializamos SharedPreferences de forma privada y segura
    private val prefs: SharedPreferences = context.getSharedPreferences("ReservaSportPrefs", Context.MODE_PRIVATE)

    // Guarda el estado de una cancha usando su ID único como clave
    fun guardarEstadoCancha(canchaId: Int, estado: String) {
        prefs.edit().putString("cancha_$canchaId", estado).apply()
    }

    // Lee el estado guardado. Si no existe, devuelve el valor inicial por defecto
    fun obtenerEstadoCancha(canchaId: Int, valorPorDefecto: String): String {
        return prefs.getString("cancha_$canchaId", valorPorDefecto) ?: valorPorDefecto
    }
}