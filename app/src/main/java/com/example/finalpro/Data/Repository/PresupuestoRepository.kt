package com.example.finalpro.Data.Repository

import com.example.finalpro.Data.Local.SessionManager
import com.example.finalpro.Data.Remote.Api.PresupuestoApi
import com.example.finalpro.Data.Remote.Dto.Request.PresupuestoRequest
import com.example.finalpro.Data.Remote.Dto.Response.PresupuestoResponse
import com.example.finalpro.Data.Remote.RetrofitClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PresupuestoRepository @Inject constructor(
    private val sessionManager: SessionManager
){
    private val api get() = RetrofitClient.build(sessionManager).create(PresupuestoApi::class.java)
    suspend fun listar(anio: Int, mes: Int, moneda: String = "COP"): Result<List<PresupuestoResponse>> = runCatching {
        val resp = api.listar(anio, mes, moneda)
        if (resp.isSuccessful) resp.body()!! else throw Exception(resp.message())
    }
    suspend fun crear(request: PresupuestoRequest): Result<PresupuestoResponse> = runCatching {
        val resp = api.crear(request)
        if (resp.isSuccessful) resp.body()!! else throw Exception(resp.message())
    }
    suspend fun actualizar(id: String, request: PresupuestoRequest): Result<PresupuestoResponse> = runCatching {
        val resp = api.actualizar(id, request)
        if (resp.isSuccessful) resp.body()!! else throw Exception(resp.message())
    }
    suspend fun eliminar(id: String): Result<Unit> = runCatching {
        val resp = api.eliminar(id)
        if (!resp.isSuccessful) throw Exception(resp.message())
    }
}
