package com.incidenciasapp.data.repository

import com.incidenciasapp.data.remote.api.IncidenciaApiService
import com.incidenciasapp.dto.incidencia.IncidenciaRequest
import com.incidenciasapp.dto.incidencia.IncidenciaResponse

class IncidenciaRepository(
    private val apiService: IncidenciaApiService
) {

    suspend fun createIncidencia(request: IncidenciaRequest): Result<IncidenciaResponse> {
        return try {
            val response = apiService.createIncidencia(request)
            if (response.exito) {
                Result.success(response)
            } else {
                Result.failure(Exception(response.mensaje ?: "Error desconocido"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}