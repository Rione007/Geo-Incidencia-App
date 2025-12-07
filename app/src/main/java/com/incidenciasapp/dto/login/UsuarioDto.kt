package com.incidenciasapp.dto.login

import com.google.gson.annotations.SerializedName


data class UsuarioDto(
    @SerializedName("iD_USUARIO")
    val ID_USUARIO: Int,

    @SerializedName("token")
    val TOKEN: String,

    @SerializedName("nombre")
    val NOMBRE: String,

    @SerializedName("email")
    val EMAIL: String,

    @SerializedName("rol")
    val ROL: String
)