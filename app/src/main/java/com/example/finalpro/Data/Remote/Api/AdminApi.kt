package com.example.finalpro.Data.Remote.Api

import com.example.finalpro.Data.Remote.Dto.Response.UsuarioResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface AdminApi {

    @GET("api/admin/usuarios")
    suspend fun listarUsuarios(): Response<List<UsuarioResponse>>

    @DELETE("api/admin/usuarios/{id}")
    suspend fun eliminarUsuario(@Path("id") id: String): Response<Unit>
}