package com.incidenciasapp.dto.login

data class LoginRequest(
    val email: String,
    val contrasenA_HASH: String
)
