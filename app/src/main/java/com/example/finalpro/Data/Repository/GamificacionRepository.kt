package com.example.finalpro.Data.Repository

import com.example.finalpro.Data.Local.SessionManager
import com.example.finalpro.Data.Remote.Api.GamificacionApi
import com.example.finalpro.Data.Remote.Dto.Response.LogroResponse
import com.example.finalpro.Data.Remote.RetrofitClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GamificacionRepository @Inject constructor(
    private val sessionManager: SessionManager
) {
    private val api get() = RetrofitClient.build(sessionManager).create(GamificacionApi::class.java)

    suspend fun listarLogros(): Result<List<LogroResponse>> = runCatching {
        val resp = api.listarLogros()
        if (resp.isSuccessful) resp.body()!! else throw Exception(resp.message())
    }

    suspend fun obtenerPuntos(): Result<Int> = runCatching {
        val resp = api.obtenerPuntos()
        if (resp.isSuccessful) resp.body()!! else throw Exception(resp.message())
    }
}