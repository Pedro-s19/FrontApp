package com.example.finalpro.Data.Repository

import com.example.finalpro.Data.Local.SessionManager
import com.example.finalpro.Data.Remote.Api.AlertaApi
import com.example.finalpro.Data.Remote.Dto.Response.AlertaResponse
import com.example.finalpro.Data.Remote.RetrofitClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlertaRepository @Inject constructor(
    private val sessionManager: SessionManager
) {
    private val api get() = RetrofitClient.build(sessionManager).create(AlertaApi::class.java)

    suspend fun obtenerAlertas(): Result<AlertaResponse> = runCatching {
        val resp = api.obtenerAlertas()
        if (resp.isSuccessful) resp.body()!! else throw Exception(resp.message())
    }
}