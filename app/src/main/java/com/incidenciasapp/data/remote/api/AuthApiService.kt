package com.incidenciasapp.data.remote.api

import com.incidenciasapp.dto.ApiResponse
import com.incidenciasapp.dto.login.LoginRequest
import com.incidenciasapp.dto.login.LoginResponse
import com.incidenciasapp.dto.registrar.RegistrarRequest
import com.incidenciasapp.dto.registrar.RegistrarResponse
import okhttp3.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("Usuario/Login")
    suspend fun login(@Body request: LoginRequest): ApiResponse<LoginResponse>

    @POST("Usuario/Registrar")
    suspend fun registrar(@Body request: RegistrarRequest): ApiResponse<RegistrarResponse>


}