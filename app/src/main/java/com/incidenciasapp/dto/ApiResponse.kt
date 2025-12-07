package com.incidenciasapp.dto

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("code")
    val code: Int,

    @SerializedName("errors")
    val errors: List<String>? = null,

    @SerializedName("data")
    val data: T? = null,

    @SerializedName("dataList")
    val dataList: List<T>? = null
)