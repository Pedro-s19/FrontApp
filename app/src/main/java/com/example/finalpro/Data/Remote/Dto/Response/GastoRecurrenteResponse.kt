package com.example.finalpro.Data.Remote.Dto.Response

data class GastoRecurrenteResponse(
    val id: String,
    val nombre: String,
    val monto: Double,
    val diaMes: Int,
    val activo: Boolean,
    val categoriaNombre: String,
    val categoriaId: String
)