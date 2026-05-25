package com.example.finalpro.Data.Repository

import com.example.finalpro.Data.Local.SessionManager
import com.example.finalpro.Data.Remote.Api.ReporteApi
import com.example.finalpro.Data.Remote.Dto.Response.DistribucionCategoriaResponse
import com.example.finalpro.Data.Remote.Dto.Response.ResumenMensualResponse
import com.example.finalpro.Data.Remote.Dto.Response.TendenciaResponse
import com.example.finalpro.Data.Remote.RetrofitClient
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReporteRepository @Inject constructor(
    private val sessionManager: SessionManager
) {
    private val api get() = RetrofitClient.build(sessionManager).create(ReporteApi::class.java)

    suspend fun tendenciaDiaria(anio: Int, mes: Int, moneda: String = "COP"): Result<TendenciaResponse> = runCatching {
        val resp = api.tendenciaDiaria(anio, mes, moneda)
        if (resp.isSuccessful) resp.body()!! else throw Exception(resp.message())
    }

    suspend fun distribucionCategorias(anio: Int, mes: Int, moneda: String = "COP"): Result<DistribucionCategoriaResponse> = runCatching {
        val resp = api.distribucionCategorias(anio, mes, moneda)
        if (resp.isSuccessful) resp.body()!! else throw Exception(resp.message())
    }

    suspend fun resumenMensual(anio: Int, mes: Int, moneda: String = "COP"): Result<ResumenMensualResponse> = runCatching {
        val resp = api.resumenMensual(anio, mes, moneda)
        if (resp.isSuccessful) resp.body()!! else throw Exception(resp.message())
    }

    suspend fun gastosDiarios(anio: Int, mes: Int, moneda: String = "COP"): Result<Map<LocalDate, Double>> = runCatching {
        val resp = api.gastosDiarios(anio, mes, moneda)
        if (resp.isSuccessful) {
            resp.body()!!.mapKeys { LocalDate.parse(it.key) }
        } else throw Exception(resp.message())
    }
}

