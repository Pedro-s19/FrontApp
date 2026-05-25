package com.example.finalpro.Data.Repository

import com.example.finalpro.Data.Local.SessionManager
import com.example.finalpro.Data.Remote.Api.MetaAhorroApi
import com.example.finalpro.Data.Remote.Dto.Request.MetaAhorroRequest
import com.example.finalpro.Data.Remote.Dto.Response.MetaAhorroResponse
import com.example.finalpro.Data.Remote.RetrofitClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MetaAhorroRepository @Inject constructor(
    private val sessionManager: SessionManager
) {
    private val api get() = RetrofitClient.build(sessionManager).create(MetaAhorroApi::class.java)

    suspend fun listar(moneda: String = "COP"): Result<List<MetaAhorroResponse>> = runCatching {
        val resp = api.listar(moneda)
        if (resp.isSuccessful) resp.body()!! else throw Exception(resp.message())
    }

    suspend fun crear(request: MetaAhorroRequest): Result<MetaAhorroResponse> = runCatching {
        val resp = api.crear(request)
        if (resp.isSuccessful) resp.body()!! else throw Exception(resp.message())
    }

    suspend fun agregarAhorro(id: String, montoCOP: Double): Result<MetaAhorroResponse> = runCatching {
        val resp = api.agregarAhorro(id, montoCOP)
        if (resp.isSuccessful) resp.body()!! else throw Exception(resp.message())
    }
    suspend fun actualizar(id: String, request: MetaAhorroRequest): Result<MetaAhorroResponse> = runCatching {
        val resp = api.actualizar(id,request)
        if(resp.isSuccessful) resp.body()!! else throw Exception(resp.message())
    }

    suspend fun eliminar(id: String): Result<Unit> = runCatching {
        val resp = api.eliminar(id)
        if (!resp.isSuccessful) throw Exception(resp.message())
    }
}
