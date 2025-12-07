package com.incidenciasapp.dto.incidencia

data class IncidenciaResponse (
    val id: Int,
    val mensaje: String,
    val codigoRespuesta: Int,
    val exito: Boolean
)