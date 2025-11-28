package com.incidenciasapp.dto.login

data class UsuarioDto(
    val ID_USUARIO: Int,
    val TOKEN: String,
    val NOMBRE: String,
    val EMAIL: String,
    val ROL: String
)
