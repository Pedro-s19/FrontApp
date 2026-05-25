package com.example.finalpro.Data.Remote.Api

import com.example.finalpro.Data.Remote.Dto.Response.AlertaResponse
import retrofit2.Response
import retrofit2.http.GET

interface AlertaApi {
    @GET("api/alertas")
    suspend fun obtenerAlertas(): Response<AlertaResponse>
}