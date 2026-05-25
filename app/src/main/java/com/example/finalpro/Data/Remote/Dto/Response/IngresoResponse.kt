package com.example.finalpro.Data.Remote.Dto.Response

data class IngresoResponse(

    val id: String,
    val monto: Double,
    val descripcion: String?,
    val fecha: String,
    val moneda: String
)
