package com.incidenciasapp.dto.incidencia

import com.google.gson.annotations.SerializedName

data class RegistrarIncidenciaResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("Mensaje") val mensaje: String,
    @SerializedName("CodigoRespuesta") val codigoRespuesta: Int,
    // En Kotlin, el "Exito => CodigoRespuesta == 0" se traduce así:
    val exito: Boolean = codigoRespuesta == 0
)
