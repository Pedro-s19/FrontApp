package com.example.finalpro.Data.Remote.Api

import com.example.finalpro.Data.Remote.Dto.Request.PresupuestoRequest
import com.example.finalpro.Data.Remote.Dto.Response.PresupuestoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PresupuestoApi {

    @GET("api/presupuestos")
    suspend fun listar(
        @Query("anio") anio: Int,
        @Query("mes") mes: Int,
        @Query("moneda") moneda: String = "COP"
    ): Response<List<PresupuestoResponse>>

    @POST("api/presupuestos")
    suspend fun crear(@Body request: PresupuestoRequest): Response<PresupuestoResponse>

    @PUT("api/presupuestos/{id}")
    suspend fun actualizar(@Path("id") id: String, @Body request: PresupuestoRequest): Response<PresupuestoResponse>

    @DELETE("api/presupuestos/{id}")
    suspend fun eliminar(@Path("id") id: String): Response<Unit>
}