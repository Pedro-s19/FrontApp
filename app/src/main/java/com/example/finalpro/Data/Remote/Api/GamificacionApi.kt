package com.example.finalpro.Data.Remote.Api

import com.example.finalpro.Data.Remote.Dto.Response.LogroResponse
import retrofit2.Response
import retrofit2.http.GET

interface GamificacionApi {
    @GET("api/gamificacion/logros")
    suspend fun listarLogros(): Response<List<LogroResponse>>

    @GET("api/gamificacion/puntos")
    suspend fun obtenerPuntos(): Response<Int>
}