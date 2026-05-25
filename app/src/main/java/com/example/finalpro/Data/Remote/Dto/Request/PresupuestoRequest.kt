package com.example.finalpro.Data.Remote.Dto.Request

data class PresupuestoRequest(
    val categoriaId: String,
    val anio: Int,
    val mes: Int,
    val limiteMonto: Double
)
