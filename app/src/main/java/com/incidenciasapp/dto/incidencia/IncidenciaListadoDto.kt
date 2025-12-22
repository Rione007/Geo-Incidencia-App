package com.incidenciasapp.dto.incidencia

import com.google.gson.annotations.SerializedName

data class IncidenciaListadoDto(

    @SerializedName("iD_INCIDENCIA")
    val idIncidencia: Int,

    @SerializedName("iD_TIPO")
    val idTipo: Int,

    @SerializedName("iD_SUBTIPO")
    val idSubtipo: Int,

    @SerializedName("fechA_INCIDENCIA")
    val fechaIncidencia: String?,  // 👈 DEBE ser nullable

    @SerializedName("descripcion")
    val descripcion: String?,

    @SerializedName("direccioN_REFERENCIA")
    val direccionReferencia: String?
)
