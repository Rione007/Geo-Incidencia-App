package com.incidenciasapp.data.repository

import com.incidenciasapp.data.remote.api.SubtipoApiService
import com.incidenciasapp.dto.Subtipo.SubtipoItem


class SubtipoRepository(
    private val api: SubtipoApiService
) {
    suspend fun obtenerSubtipos(): Result<List<SubtipoItem>> {
        return try {
            val response = api.obtenerSubtipos()
            val subtipos = response.data ?: emptyList()
            Result.success(subtipos)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}