package com.example.finalpro.Data.Remote.Dto.Request

data class MetaAhorroRequest(
    val nombre: String,
    val montoObjetivo: Double,
    val fechaLimite: String?
)
