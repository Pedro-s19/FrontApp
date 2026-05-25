package com.example.finalpro.Data.Remote.Dto.Response

data class TendenciaResponse(

    val etiquetas: List<String>,
    val valores: List<Double>,
    val moneda: String
)
