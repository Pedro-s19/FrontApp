package com.example.finalpro.Data.Remote.Api

import com.example.finalpro.Data.Remote.Dto.Request.LoginRequest
import com.example.finalpro.Data.Remote.Dto.Request.RegistroRequest
import com.example.finalpro.Data.Remote.Dto.Response.JwtResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<JwtResponse>

    @POST("api/auth/registro")
    suspend fun register(@Body request: RegistroRequest): Response<Unit>
}