package com.incidenciasapp.data.repository

import com.incidenciasapp.data.remote.api.TipoApiService
import com.incidenciasapp.dto.Tipo.TipoItem

class TipoRepository(
    private val api: TipoApiService
) {
    suspend fun obtenerTipos(): Result<List<TipoItem>> {
        return try {
            val response = api.obtenerTipos()
            val tipos = response.data ?: emptyList()
            Result.success(tipos)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}