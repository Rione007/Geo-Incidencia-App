package com.incidenciasapp.data.remote.api

import com.incidenciasapp.dto.ApiResponse
import com.incidenciasapp.dto.incidencia.IncidenciaRequest
import com.incidenciasapp.dto.incidencia.IncidenciaResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface IncidenciaApiService {

    @POST("Incidencia")
    suspend fun createIncidencia(@Body request: IncidenciaRequest): IncidenciaResponse

}