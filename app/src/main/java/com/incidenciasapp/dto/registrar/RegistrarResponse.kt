package com.incidenciasapp.dto.registrar

data class RegistrarResponse(
    val id: Int,
    val mensaje: String,
    val codigoRespuesta: Int,
    val exito: Boolean
)