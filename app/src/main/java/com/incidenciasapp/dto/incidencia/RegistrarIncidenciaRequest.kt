package com.incidenciasapp.dto.incidencia

import com.google.gson.annotations.SerializedName

data class RegistrarIncidenciaRequest(
    @SerializedName("IdUsuario") val idUsuario: Int,
    @SerializedName("Latitud") val latitud: Double,
    @SerializedName("Longitud") val longitud: Double,
    @SerializedName("IdTipo") val idTipo: Int,
    @SerializedName("IdSubtipo") val idSubtipo: Int,
    @SerializedName("Descripcion") val descripcion: String?,
    @SerializedName("DIRECCION_REFERENCIA") val direccionReferencia: String?,
    @SerializedName("FOTO_URL1") val fotoUrl1: String?,
    @SerializedName("FOTO_URL2") val fotoUrl2: String?,
    @SerializedName("FOTO_URL3") val fotoUrl3: String?,
    @SerializedName("FechaIncidencia") val fechaIncidencia: String // ISO 8601
)
