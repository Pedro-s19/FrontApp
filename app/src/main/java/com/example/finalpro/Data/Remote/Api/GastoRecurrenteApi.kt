package com.example.finalpro.Data.Remote.Api

import com.example.finalpro.Data.Remote.Dto.Request.GastoRecurrenteRequest
import com.example.finalpro.Data.Remote.Dto.Response.GastoRecurrenteResponse
import retrofit2.Response
import retrofit2.http.*

interface GastoRecurrenteApi {
    @GET("api/recurrentes")
    suspend fun listar(): Response<List<GastoRecurrenteResponse>>

    @POST("api/recurrentes")
    suspend fun crear(@Body request: GastoRecurrenteRequest): Response<GastoRecurrenteResponse>

    @PUT("api/recurrentes/{id}")
    suspend fun actualizar(@Path("id") id: String, @Body request: GastoRecurrenteRequest): Response<GastoRecurrenteResponse>

    @DELETE("api/recurrentes/{id}")
    suspend fun eliminar(@Path("id") id: String): Response<Unit>
}