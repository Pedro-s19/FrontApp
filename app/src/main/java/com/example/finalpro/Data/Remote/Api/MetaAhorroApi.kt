package com.example.finalpro.Data.Remote.Api

import com.example.finalpro.Data.Remote.Dto.Request.MetaAhorroRequest
import com.example.finalpro.Data.Remote.Dto.Response.MetaAhorroResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface MetaAhorroApi {

    @GET("api/metas-ahorro")
    suspend fun listar(@Query("moneda") moneda: String = "COP"): Response<List<MetaAhorroResponse>>

    @POST("api/metas-ahorro")
    suspend fun crear(@Body request: MetaAhorroRequest): Response<MetaAhorroResponse>

    @POST("api/metas-ahorro/{id}/agregar-ahorro")
    suspend fun agregarAhorro(
        @Path("id") id: String,
        @Query("montoCOP") montoCOP: Double
    ): Response<MetaAhorroResponse>

    @PUT("api/metas-ahorro/{id}")
    suspend fun actualizar(
        @Path("id") id: String,
        @Body request: MetaAhorroRequest
    ): Response<MetaAhorroResponse>

    @DELETE("api/metas-ahorro/{id}")
    suspend fun eliminar(@Path("id") id: String): Response<Unit>
}