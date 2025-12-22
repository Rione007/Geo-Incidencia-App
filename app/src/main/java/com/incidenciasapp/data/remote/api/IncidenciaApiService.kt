package com.incidenciasapp.data.remote.api

import com.incidenciasapp.dto.ApiResponse
import com.incidenciasapp.dto.incidencia.IncidenciaDto
import com.incidenciasapp.dto.incidencia.IncidenciaListadoDto
import com.incidenciasapp.dto.incidencia.IncidenciaRequest
import com.incidenciasapp.dto.incidencia.IncidenciaResponse
import com.incidenciasapp.dto.incidencia.RegistrarIncidenciaRequest
import com.incidenciasapp.dto.incidencia.RegistrarIncidenciaResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface IncidenciaApiService {

    // 1. Registrar incidencia
    @POST("Incidencia/Registrar")
    suspend fun registrarIncidencia(
        @Body request: IncidenciaRequest
    ): IncidenciaResponse

    // 2. Listar incidencias con filtros (Query Params)
    @GET("Incidencia/listar")
    suspend fun listarIncidencias(
        @Query("tipo") tipo: Int?,
        @Query("subtipo") subtipo: Int?,
        @Query("fechaDesde") fechaDesde: String?, // Formato ISO 8601
        @Query("fechaHasta") fechaHasta: String?,
        @Query("limit") limit: Int = 50
    ): Response<ApiResponse<List<IncidenciaListadoDto>>>

    // 3. Buscar por área (Caja de coordenadas)
    @GET("Incidencia/buscarPorArea")
    suspend fun buscarPorArea(
        @Query("minLat") minLat: Double,
        @Query("maxLat") maxLat: Double,
        @Query("minLng") minLng: Double,
        @Query("maxLng") maxLng: Double,
        @Query("tipos") tipos: List<Int>?,
        @Query("subtipos") subtipos: List<Int>?,
        @Query("dias") dias: Int?
    ): Response<ApiResponse<List<IncidenciaDto>>>


    // 6. Listar incidencias por usuario (Path Variable)
    @GET("Incidencia/ListarPorUsuario/{usuarioId}")
    suspend fun listarPorUsuario(
        @Path("usuarioId") usuarioId: Int
    ): Response<ApiResponse<List<IncidenciaListadoDto>>>

    // 7. Obtener incidencia por Id
    @GET("Incidencia/Obtener/{id}")
    suspend fun obtenerPorId(
        @Path("id") id: Int
    ): Response<ApiResponse<IncidenciaDto>>

}