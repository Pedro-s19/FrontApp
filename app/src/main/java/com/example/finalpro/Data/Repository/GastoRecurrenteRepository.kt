package com.example.finalpro.Data.Repository

import com.example.finalpro.Data.Local.SessionManager
import com.example.finalpro.Data.Remote.Api.GastoRecurrenteApi
import com.example.finalpro.Data.Remote.Dto.Request.GastoRecurrenteRequest
import com.example.finalpro.Data.Remote.Dto.Response.GastoRecurrenteResponse
import com.example.finalpro.Data.Remote.RetrofitClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GastoRecurrenteRepository @Inject constructor(
    private val sessionManager: SessionManager
) {
    private val api get() = RetrofitClient.build(sessionManager).create(GastoRecurrenteApi::class.java)

    suspend fun listar(): Result<List<GastoRecurrenteResponse>> = runCatching {
        val resp = api.listar()
        if (resp.isSuccessful) resp.body()!! else throw Exception(resp.message())
    }

    suspend fun crear(request: GastoRecurrenteRequest): Result<GastoRecurrenteResponse> = runCatching {
        val resp = api.crear(request)
        if (resp.isSuccessful) resp.body()!! else throw Exception(resp.message())
    }

    suspend fun actualizar(id: String, request: GastoRecurrenteRequest): Result<GastoRecurrenteResponse> = runCatching {
        val resp = api.actualizar(id, request)
        if (resp.isSuccessful) resp.body()!! else throw Exception(resp.message())
    }

    suspend fun eliminar(id: String): Result<Unit> = runCatching {
        val resp = api.eliminar(id)
        if (!resp.isSuccessful) throw Exception(resp.message())
    }
}