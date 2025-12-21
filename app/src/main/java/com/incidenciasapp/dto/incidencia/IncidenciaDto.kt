package com.incidenciasapp.dto.incidencia

import com.google.gson.annotations.SerializedName

data class IncidenciaDto(
    @SerializedName("ID_INCIDENCIA")
    val idIncidencia: Int,

    @SerializedName("ID_TIPO")
    val idTipo: Int,

    @SerializedName("ID_SUBTIPO")
    val idSubtipo: Int,

    @SerializedName("ID_USUARIO")
    val idUsuario: Int,

    @SerializedName("FECHA_INCIDENCIA")
    val fechaIncidencia: String,

    @SerializedName("FECHA_REGISTRO")
    val fechaRegistro: String,

    @SerializedName("LATITUD")
    val latitud: Double,

    @SerializedName("LONGITUD")
    val longitud: Double,

    @SerializedName("DESCRIPCION")
    val descripcion: String?,

    @SerializedName("FOTO_URL1")
    val fotoUrl1: String?,

    @SerializedName("FOTO_URL2")
    val fotoUrl2: String?,

    @SerializedName("FOTO_URL3")
    val fotoUrl3: String?,

    @SerializedName("DIRECCION_REFERENCIA")
    val direccionReferencia: String?,

    @SerializedName("ESTADO")
    val estado: Boolean,

    @SerializedName("DISTANCIA")
    val distancia: String? // Se llena solo en búsquedas por radio
)