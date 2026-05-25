package com.example.finalpro.Data.Remote.Dto.Response

data class ResumenMensualResponse(

    val totalGastos: Double,
    val totalIngresos: Double,
    val balance: Double,
    val presupuestoRestante: Double?,
    val porcentajePresupuestoUsado: Double?,
    val moneda: String
)
