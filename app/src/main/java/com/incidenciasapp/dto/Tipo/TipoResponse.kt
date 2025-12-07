package com.incidenciasapp.dto.Tipo


data class TipoResponse(
    val success: Boolean,
    val message: String?,
    val code: Int,
    val errors: Any?,
    val data: List<TipoItem>?,
    val dataList: Any?
)