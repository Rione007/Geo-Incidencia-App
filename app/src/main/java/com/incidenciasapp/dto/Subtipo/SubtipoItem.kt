package com.incidenciasapp.dto.Subtipo

import com.google.gson.annotations.SerializedName

data class SubtipoItem(
    @SerializedName("iD_SUBTIPO")
    val idSubtipo: Int,
    @SerializedName("iD_TIPO")
    val idTipo: Int,
    val nombre: String
)