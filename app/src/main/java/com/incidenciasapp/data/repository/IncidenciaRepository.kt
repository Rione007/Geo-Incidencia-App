package com.incidenciasapp.data.repository

import com.incidenciasapp.data.remote.api.IncidenciaApiService
import com.incidenciasapp.dto.incidencia.IncidenciaListadoDto
import com.incidenciasapp.dto.incidencia.IncidenciaRequest
import com.incidenciasapp.dto.incidencia.IncidenciaResponse

class IncidenciaRepository(
    private val apiService: IncidenciaApiService
) {

    suspend fun createIncidencia(request: IncidenciaRequest): Result<IncidenciaResponse> {
        return try {
            val response = apiService.registrarIncidencia(request)
            if (response.exito) {
                Result.success(response)
            } else {
                Result.failure(Exception(response.mensaje ?: "Error desconocido"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    // En IncidenciaRepository.kt
    suspend fun listarIncidencias(tipo: Int? = null): Result<List<IncidenciaListadoDto>> {
        return try {
            val response = apiService.listarIncidencias(tipo, null, null, null)
            if (response.isSuccessful) {
                // Accedemos a .data porque tu backend devuelve ApiResponse<T>
                val lista = response.body()?.data ?: emptyList()
                Result.success(lista)
            } else {
                Result.failure(Exception("Error : ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}