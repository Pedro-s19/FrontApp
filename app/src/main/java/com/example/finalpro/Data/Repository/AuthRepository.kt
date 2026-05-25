package com.example.finalpro.Data.Repository

import com.example.finalpro.Data.Local.SessionManager
import com.example.finalpro.Data.Remote.Api.AuthApi
import com.example.finalpro.Data.Remote.Dto.Request.LoginRequest
import com.example.finalpro.Data.Remote.Dto.Request.RegistroRequest
import com.example.finalpro.Data.Remote.Dto.Response.JwtResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val api: AuthApi,
    private val sessionManager: SessionManager
) {
    suspend fun login(request: LoginRequest): Result<JwtResponse> = runCatching {
        val resp = api.login(request)
        if (resp.isSuccessful) {
            resp.body() ?: throw Exception("Respuesta vacia del servidor")
        } else {
            throw Exception("Error ${resp.code()}: ${resp.message()}")
        }
    }

    suspend fun register(request: RegistroRequest): Result<Unit> = runCatching {
        val resp = api.register(request)
        if (!resp.isSuccessful) {
            val errorBody = resp.errorBody()?.string() ?: resp.message()
            throw Exception(errorBody)
        }
    }
}