package com.example.finalpro.Data.Remote.Dto.Response

data class MetaAhorroResponse(

    val id: String,
    val nombre: String,
    val montoObjetivo: Double,
    val montoActual: Double,
    val porcentajeCompletado: Double,
    val fechaLimite: String?,
    val moneda: String
)
