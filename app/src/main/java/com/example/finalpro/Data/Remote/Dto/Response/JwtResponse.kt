package com.example.finalpro.Data.Remote.Dto.Response

data class JwtResponse(
    val token: String,
    val tipo: String,
    val email: String,
    val rol: String
)
