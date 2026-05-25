package com.example.finalpro.Data.Repository

import com.example.finalpro.Data.Local.SessionManager
import com.example.finalpro.Data.Remote.Api.AdminApi
import com.example.finalpro.Data.Remote.Dto.Response.UsuarioResponse
import com.example.finalpro.Data.Remote.RetrofitClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdminRepository @Inject constructor(
    private val sessionManager: SessionManager
) {
    private val api get() = RetrofitClient.build(sessionManager).create(AdminApi::class.java)

    suspend fun listarUsuarios(): Result<List<UsuarioResponse>> = runCatching {
        val resp = api.listarUsuarios()
        if (resp.isSuccessful) resp.body()!! else throw Exception(resp.message())
    }

    suspend fun eliminarUsuario(id: String): Result<Unit> = runCatching {
        val resp = api.eliminarUsuario(id)
        if (!resp.isSuccessful) throw Exception(resp.message())
    }
}