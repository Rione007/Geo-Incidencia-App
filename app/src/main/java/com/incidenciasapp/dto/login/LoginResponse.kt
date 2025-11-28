package com.incidenciasapp.dto.login

data class LoginResponse(
    val usuario: UsuarioDto,
    val mensaje: String,
    val codigoRespuesta: Int,
    val exito: Boolean
)
