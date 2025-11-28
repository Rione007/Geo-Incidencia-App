package com.incidenciasapp.data.remote.api

import com.incidenciasapp.dto.ApiResponse
import com.incidenciasapp.dto.login.LoginRequest
import com.incidenciasapp.dto.login.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("Usuario/Login")
    suspend fun login(
        @Body request: LoginRequest
    ): ApiResponse<LoginResponse>

}