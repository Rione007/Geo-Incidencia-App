package com.incidenciasapp.dto.Subtipo

import com.google.gson.annotations.SerializedName
import com.incidenciasapp.dto.Tipo.TipoItem

data class SubtipoResponse (
    val success: Boolean,
    val message: String?,
    val code: Int,
    val errors: Any?,
    val data: List<SubtipoItem>?,
    val dataList: Any?
)
