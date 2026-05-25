package com.example.finalpro.Data.Remote.Dto.Request

data class GastoRequest(
    val monto: Double,
    val descripcion: String?,
    val fecha: String,
    val categoriaId: String
)
