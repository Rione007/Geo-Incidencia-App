package com.incidenciasapp.dto.incidencia

import com.google.gson.annotations.SerializedName

data class IncidenciaDto(

    @SerializedName("iD_INCIDENCIA")
    val idIncidencia: Int,

    @SerializedName("iD_TIPO")
    val idTipo: Int,

    @SerializedName("iD_SUBTIPO")
    val idSubtipo: Int,

    @SerializedName("iD_USUARIO")
    val idUsuario: Int,

    @SerializedName("fechA_INCIDENCIA")
    val fechaIncidencia: String?,

    @SerializedName("fechA_REGISTRO")
    val fechaRegistro: String?,

    @SerializedName("latitud")
    val latitud: Double,

    @SerializedName("longitud")
    val longitud: Double,

    @SerializedName("descripcion")
    val descripcion: String?,

    @SerializedName("fotO_URL1")
    val fotoUrl1: String?, //este no se muestra

    @SerializedName("fotO_URL2")
    val fotoUrl2: String?, //este no se muestra

    @SerializedName("fotO_URL3")
    val fotoUrl3: String?, //este no se muestra

    @SerializedName("direccioN_REFERENCIA")
    val direccionReferencia: String?,

    @SerializedName("estado")
    val estado: Boolean, //este no se muestra

    @SerializedName("distancia")
    val distancia: String?  //este no se muestra
)
