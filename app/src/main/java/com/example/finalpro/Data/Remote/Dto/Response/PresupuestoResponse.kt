package com.example.finalpro.Data.Remote.Dto.Response

data class PresupuestoResponse(

    val id: String,
    val categoriaId: String,
    val categoriaNombre: String,
    val anio: Int,
    val mes: Int,
    val limiteMonto: Double,
    val gastadoMonto: Double,
    val moneda: String
)
