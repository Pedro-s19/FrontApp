package com.example.finalpro.Data.Remote.Dto.Request

data class GastoRecurrenteRequest(
    val nombre: String,
    val monto: Double,
    val diaMes: Int,
    val categoriaId: String
)