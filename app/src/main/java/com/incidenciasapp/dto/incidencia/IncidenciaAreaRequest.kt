package com.incidenciasapp.dto.incidencia

data class IncidenciaAreaRequest(
    val minLat: Double,
    val maxLat: Double,
    val minLng: Double,
    val maxLng: Double,
    val tipos: List<Int>? = null,
    val subtipos: List<Int>? = null,
    val dias: Int? = null
)