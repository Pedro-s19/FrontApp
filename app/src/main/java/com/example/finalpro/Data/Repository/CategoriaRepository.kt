package com.example.finalpro.Data.Repository

import com.example.finalpro.Data.Local.SessionManager
import com.example.finalpro.Data.Remote.Api.CategoriaApi
import com.example.finalpro.Data.Remote.Dto.Request.CategoriaRequest
import com.example.finalpro.Data.Remote.Dto.Response.CategoriaResponse
import com.example.finalpro.Data.Remote.RetrofitClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoriaRepository @Inject constructor(
    private val sessionManager: SessionManager
) {
    private val api get() = RetrofitClient.build(sessionManager).create(CategoriaApi::class.java)

    suspend fun listar(): Result<List<CategoriaResponse>> = runCatching {
        val resp = api.listar()
        if (resp.isSuccessful) resp.body()!! else throw Exception(resp.message())
    }

    suspend fun crear(request: CategoriaRequest): Result<CategoriaResponse> = runCatching {
        val resp = api.crear(request)
        if (resp.isSuccessful) resp.body()!! else throw Exception(resp.message())
    }

    suspend fun eliminar(id: String): Result<Unit> = runCatching {
        val resp = api.eliminar(id)
        if (!resp.isSuccessful) throw Exception(resp.message())
    }
}
