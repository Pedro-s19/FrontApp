package com.example.finalpro.Data.Repository

import com.example.finalpro.Data.Local.SessionManager
import com.example.finalpro.Data.Remote.Api.GastoApi
import com.example.finalpro.Data.Remote.Dto.Request.GastoRequest
import com.example.finalpro.Data.Remote.Dto.Response.GastoResponse
import com.example.finalpro.Data.Remote.RetrofitClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GastoRepository @Inject constructor(
    private val sessionManager: SessionManager
) {
    private val api get() = RetrofitClient.build(sessionManager).create(GastoApi::class.java)

    suspend fun listar(moneda: String = "COP"): Result<List<GastoResponse>> = runCatching {
        val resp = api.listar(moneda)
        if (resp.isSuccessful) resp.body()!! else throw Exception("Error ${resp.code()}")
    }

    suspend fun crear(request: GastoRequest): Result<GastoResponse> = runCatching {
        val resp = api.crear(request)
        if (resp.isSuccessful) resp.body()!! else throw Exception(resp.message())
    }

    suspend fun actualizar(id: String, request: GastoRequest): Result<GastoResponse> = runCatching {
        val resp = api.actualizar(id, request)
        if (resp.isSuccessful) resp.body()!! else throw Exception(resp.message())
    }

    suspend fun eliminar(id: String): Result<Unit> = runCatching {
        val resp = api.eliminar(id)
        if (!resp.isSuccessful) throw Exception(resp.message())
    }
}