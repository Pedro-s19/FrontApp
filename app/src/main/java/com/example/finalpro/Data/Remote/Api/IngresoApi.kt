package com.example.finalpro.Data.Remote.Api

import com.example.finalpro.Data.Remote.Dto.Request.IngresoRequest
import com.example.finalpro.Data.Remote.Dto.Response.IngresoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface IngresoApi {

    @GET("api/ingresos")
    suspend fun listar(@Query("moneda") moneda: String = "COP"): Response<List<IngresoResponse>>

    @POST("api/ingresos")
    suspend fun crear(@Body request: IngresoRequest): Response<IngresoResponse>

    @PUT("api/ingresos/{id}")
    suspend fun actualizar(@Path("id") id: String, @Body request: IngresoRequest): Response<IngresoResponse>

    @DELETE("api/ingresos/{id}")
    suspend fun eliminar(@Path("id") id: String): Response<Unit>
}