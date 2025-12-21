package com.incidenciasapp.dto.incidencia

import com.google.gson.annotations.SerializedName

data class IncidenciaListadoDto(
    @SerializedName("ID_INCIDENCIA")
    val idIncidencia: Int,

    @SerializedName("ID_TIPO")
    val idTipo: Int,

    @SerializedName("ID_SUBTIPO")
    val idSubtipo: Int,

    @SerializedName("ID_USUARIO")
    val idUsuario: Int,

    @SerializedName("FECHA_INCIDENCIA")
    val fechaIncidencia: String, // Se recibe como String ISO 8601

    @SerializedName("DESCRIPCION")
    val descripcion: String?,

    @SerializedName("DIRECCION_REFERENCIA")
    val direccionReferencia: String?
)