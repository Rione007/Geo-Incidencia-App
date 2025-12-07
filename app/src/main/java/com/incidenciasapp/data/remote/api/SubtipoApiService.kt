package com.incidenciasapp.data.remote.api

import com.incidenciasapp.dto.Subtipo.SubtipoResponse
import retrofit2.http.GET

interface SubtipoApiService {

    @GET("Subtipo")
    suspend fun obtenerSubtipos(): SubtipoResponse
}