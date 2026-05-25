package com.example.finalpro.Data.Remote.Dto.Response

data class GastoResponse(

    val id: String,
    val monto: Double,
    val descripcion: String?,
    val fecha: String,
    val moneda: String,
    val categoria: CategoriaResponse?
)
