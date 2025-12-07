package com.incidenciasapp.data.repository

import com.incidenciasapp.data.remote.api.AuthApiService
import com.incidenciasapp.dto.login.LoginRequest
import com.incidenciasapp.dto.login.LoginResponse
import com.incidenciasapp.dto.registrar.RegistrarRequest
import com.incidenciasapp.dto.registrar.RegistrarResponse

class AuthRepository(
    private val api: AuthApiService
) {

    suspend fun login(cuenta: String, contrasena: String): Result<LoginResponse> {
        return try {
            val request = LoginRequest(cuenta, contrasena)
            val response = api.login(request)

            if (response.success && response.data != null) {
                if (response.data.exito) {
                    Result.success(response.data)
                } else {
                    Result.failure(Exception(response.data.mensaje))
                }
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun registrar(nombre: String, email: String, contrasena: String): Result<RegistrarResponse> {
        return try {
            val request = RegistrarRequest(nombre, email, contrasena)
            val response = api.registrar(request)

            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception("Error: ${response.message} - ${response.errors}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
