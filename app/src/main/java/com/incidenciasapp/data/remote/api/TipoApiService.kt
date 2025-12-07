package com.incidenciasapp.data.remote.api

import com.incidenciasapp.dto.Tipo.TipoResponse
import retrofit2.http.GET

interface TipoApiService {

    @GET("Tipo")
    suspend fun obtenerTipos(): TipoResponse
}