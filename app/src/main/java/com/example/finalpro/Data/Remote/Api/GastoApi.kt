package com.example.finalpro.Data.Remote.Api

import com.example.finalpro.Data.Remote.Dto.Request.GastoRequest
import com.example.finalpro.Data.Remote.Dto.Response.GastoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface GastoApi {

    @GET("api/gastos")
    suspend fun listar(@Query("moneda") moneda: String = "COP"): Response<List<GastoResponse>>

    @GET("api/gastos/{id}")
    suspend fun obtener(@Path("id") id: String): Response<GastoResponse>

    @POST("api/gastos")
    suspend fun crear(@Body request: GastoRequest): Response<GastoResponse>

    @PUT("api/gastos/{id}")
    suspend fun actualizar(@Path("id") id: String, @Body request: GastoRequest): Response<GastoResponse>

    @DELETE("api/gastos/{id}")
    suspend fun eliminar(@Path("id") id: String): Response<Unit>
}