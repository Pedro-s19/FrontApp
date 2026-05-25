package com.example.finalpro.Data.Remote.Dto.Response

data class LogroResponse(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val icono: String,
    val desbloqueado: Boolean,
    val puntos: Int
)