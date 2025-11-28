package com.incidenciasapp.data.repository

import com.incidenciasapp.data.remote.api.AuthApiService
import com.incidenciasapp.dto.login.LoginRequest
import com.incidenciasapp.dto.login.LoginResponse

class AuthRepository(
    private val api: AuthApiService
) {

    suspend fun login(cuenta: String, contrasena: String): Result<LoginResponse> {
        return try {
            val request = LoginRequest(cuenta, contrasena)
            val response = api.login(request)

            if (response.success && response.data != null && response.data.exito) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
