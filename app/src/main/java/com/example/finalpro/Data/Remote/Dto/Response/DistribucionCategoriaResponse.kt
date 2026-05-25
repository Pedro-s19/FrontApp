package com.example.finalpro.Data.Remote.Dto.Response

data class DistribucionCategoriaResponse(
    val items: List<ItemCategoriaGasto>,
    val moneda: String
){
    data class ItemCategoriaGasto(
        val categoriaNombre: String,
        val total: Double,
        val porcentaje: Double
    )
}
