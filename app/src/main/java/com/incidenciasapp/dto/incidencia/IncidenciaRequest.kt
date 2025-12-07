package com.incidenciasapp.dto.incidencia

import com.google.gson.annotations.SerializedName

data class IncidenciaRequest(
    @SerializedName("idUsuario")
    val idUsuario: Int,

    @SerializedName("latitud")
    val latitud: Double,

    @SerializedName("longitud")
    val longitud: Double,

    @SerializedName("idTipo")
    val idTipo: Int,

    @SerializedName("idSubtipo")
    val idSubtipo: Int,

    @SerializedName("descripcion")
    val descripcion: String?,

    @SerializedName("direccioN_REFERENCIA")
    val direccionReferencia: String?,

    @SerializedName("fotO_URL1")
    val fotoUrl1: String?,

    @SerializedName("fotO_URL2")
    val fotoUrl2: String?,

    @SerializedName("fotO_URL3")
    val fotoUrl3: String?,

    @SerializedName("fechaIncidencia")
    val fechaIncidencia: String
)
