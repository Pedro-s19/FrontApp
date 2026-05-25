package com.example.finalpro.Data.Remote.Api

import com.example.finalpro.Data.Remote.Dto.Response.DistribucionCategoriaResponse
import com.example.finalpro.Data.Remote.Dto.Response.ResumenMensualResponse
import com.example.finalpro.Data.Remote.Dto.Response.TendenciaResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ReporteApi {

    @GET("api/reportes/tendencia")
    suspend fun tendenciaDiaria(
        @Query("anio") anio: Int,
        @Query("mes") mes: Int,
        @Query("moneda") moneda: String = "COP"
    ): Response<TendenciaResponse>

    @GET("api/reportes/categorias")
    suspend fun distribucionCategorias(
        @Query("anio") anio: Int,
        @Query("mes") mes: Int,
        @Query("moneda") moneda: String = "COP"
    ): Response<DistribucionCategoriaResponse>

    @GET("api/reportes/resumen-mensual")
    suspend fun resumenMensual(
        @Query("anio") anio: Int,
        @Query("mes") mes: Int,
        @Query("moneda") moneda: String = "COP"
    ): Response<ResumenMensualResponse>

    @GET("api/reportes/gastos-diarios")
    suspend fun gastosDiarios(
        @Query("anio") anio: Int,
        @Query("mes") mes: Int,
        @Query("moneda") moneda: String = "COP"
    ): Response<Map<String, Double>>
}