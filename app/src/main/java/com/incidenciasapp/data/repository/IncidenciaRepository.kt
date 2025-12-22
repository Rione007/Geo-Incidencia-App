package com.incidenciasapp.data.repository

import com.incidenciasapp.data.remote.api.IncidenciaApiService
import com.incidenciasapp.dto.incidencia.IncidenciaAreaRequest
import com.incidenciasapp.dto.incidencia.IncidenciaDto
import com.incidenciasapp.dto.incidencia.IncidenciaListadoDto
import com.incidenciasapp.dto.incidencia.IncidenciaListadoRequest
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
    suspend fun listarIncidencias(request: IncidenciaListadoRequest): Result<List<IncidenciaListadoDto>> {
        return try {
            val response = apiService.listarIncidencias(request.tipo,request.subtipo,request.fechaDesde,request.fechaHasta,request.limit)
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

    suspend fun obtenerIncidenciaPorId(id: Int): Result<IncidenciaDto> {
        return try {
            val response = apiService.obtenerPorId(id)

            if (response.isSuccessful) {
                val body = response.body()

                if (body?.success == true && body.data != null) {
                    Result.success(body.data)
                } else {
                    Result.failure(Exception(body?.message ?: "Incidencia no encontrada"))
                }
            } else {
                Result.failure(Exception("Error HTTP: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun listarIncidenciasPorUsuario(
        usuarioId: Int
    ): Result<List<IncidenciaListadoDto>> {
        return try {
            val response = apiService.listarPorUsuario(usuarioId)

            if (response.isSuccessful) {
                val body = response.body()

                if (body?.success == true) {
                    Result.success(body.data ?: emptyList())
                } else {
                    Result.failure(Exception(body?.message ?: "No se encontraron incidencias"))
                }
            } else {
                Result.failure(Exception("Error HTTP: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun buscarIncidenciasPorArea(
        request: IncidenciaAreaRequest
    ): Result<List<IncidenciaDto>> {
        return try {
            val response = apiService.buscarPorArea(
                minLat = request.minLat,
                maxLat = request.maxLat,
                minLng = request.minLng,
                maxLng = request.maxLng,
                tipos = request.tipos,
                subtipos = request.subtipos,
                dias = request.dias
            )

            if (response.isSuccessful) {
                val body = response.body()

                if (body?.success == true) {
                    Result.success(body.data ?: emptyList())
                } else {
                    Result.failure(Exception(body?.message ?: "Sin resultados en el área"))
                }
            } else {
                Result.failure(Exception("Error HTTP: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }




}