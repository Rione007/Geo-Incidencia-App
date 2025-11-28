package com.incidenciasapp.data.remote.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://incidenciaciudadana-cvbdc6gsakfkbzfx.brazilsouth-01.azurewebsites.net/api/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private fun getOkHttpClient(tokenProvider: () -> String?): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val req = chain.request()
                val token = tokenProvider()
                val newReq = if (token != null) {
                    req.newBuilder()
                        .header("Authorization", "Bearer $token")
                        .build()
                } else req
                chain.proceed(newReq)
            }
            .build()
    }

    fun <T> create(service: Class<T>, tokenProvider: () -> String?): T {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client( getOkHttpClient(tokenProvider) )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(service)
    }
}