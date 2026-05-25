package com.example.finalpro.Data.Remote.Api

import com.example.finalpro.Data.Remote.Dto.Request.CategoriaRequest
import com.example.finalpro.Data.Remote.Dto.Response.CategoriaResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CategoriaApi {

    @GET("api/categorias")
    suspend fun listar(): Response<List<CategoriaResponse>>

    @POST("api/categorias")
    suspend fun crear(@Body request: CategoriaRequest): Response<CategoriaResponse>

    @DELETE("api/categorias/{id}")
    suspend fun eliminar(@Path("id") id: String): Response<Unit>
}