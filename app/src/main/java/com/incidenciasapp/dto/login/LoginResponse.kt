package com.incidenciasapp.dto.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("usuario")
    val usuario: UsuarioDto,

    @SerializedName("mensaje")
    val mensaje: String,

    @SerializedName("codigoRespuesta")
    val codigoRespuesta: Int,

    @SerializedName("exito")
    val exito: Boolean
)