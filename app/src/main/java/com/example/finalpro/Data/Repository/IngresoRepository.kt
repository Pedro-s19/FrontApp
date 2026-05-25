package com.example.finalpro.Data.Repository

import com.example.finalpro.Data.Local.SessionManager
import com.example.finalpro.Data.Remote.Api.IngresoApi
import com.example.finalpro.Data.Remote.Dto.Request.IngresoRequest
import com.example.finalpro.Data.Remote.Dto.Response.IngresoResponse
import com.example.finalpro.Data.Remote.RetrofitClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IngresoRepository @Inject constructor(
    private val sessionManager: SessionManager
) {
    private val api get() = RetrofitClient.build(sessionManager).create(IngresoApi::class.java)

    suspend fun listar(moneda: String = "COP"): Result<List<IngresoResponse>> = runCatching {
        val resp = api.listar(moneda)
        if (resp.isSuccessful) resp.body()!! else throw Exception("Error ${resp.code()}")
    }

    suspend fun crear(request: IngresoRequest): Result<IngresoResponse> = runCatching {
        val resp = api.crear(request)
        if (resp.isSuccessful) resp.body()!! else throw Exception(resp.message())
    }

    suspend fun actualizar(id: String, request: IngresoRequest): Result<IngresoResponse> = runCatching {
        val resp = api.actualizar(id, request)
        if (resp.isSuccessful) resp.body()!! else throw Exception(resp.message())
    }

    suspend fun eliminar(id: String): Result<Unit> = runCatching {
        val resp = api.eliminar(id)
        if (!resp.isSuccessful) throw Exception(resp.message())
    }
}
