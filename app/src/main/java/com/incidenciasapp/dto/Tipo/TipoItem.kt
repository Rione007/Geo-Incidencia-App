package com.incidenciasapp.dto.Tipo

import com.google.gson.annotations.SerializedName

data class TipoItem(
    @SerializedName("iD_TIPO")
    val idTipo: Int,
    val nombre: String,
    val descripcion: String
)