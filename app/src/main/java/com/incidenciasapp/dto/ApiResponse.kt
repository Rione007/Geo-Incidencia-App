package com.incidenciasapp.dto

data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val code: Int,
    val errors: Map<String, List<String>>?,
    val data: T?,
    val dataList: List<T>?
)

