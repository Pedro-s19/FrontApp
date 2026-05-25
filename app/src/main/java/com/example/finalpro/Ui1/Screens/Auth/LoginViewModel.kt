package com.example.finalpro.Ui1.Screens.Auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalpro.Data.Local.SessionManager
import com.example.finalpro.Data.Remote.Dto.Request.LoginRequest
import com.example.finalpro.Data.Repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthState {
    object Idle    : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val msg: String) : AuthState()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
    val state: StateFlow<AuthState> = _state.asStateFlow()

    fun login(email: String, contrasena: String) {
        if (email.isBlank() || contrasena.isBlank()) {
            _state.value = AuthState.Error("Por favor completa todos los campos")
            return
        }
        viewModelScope.launch {
            _state.value = AuthState.Loading
            authRepository.login(LoginRequest(email.trim(), contrasena))
                .onSuccess { jwt ->
                    sessionManager.saveSession(jwt.token, jwt.email,jwt.rol)
                    _state.value = AuthState.Success
                }
                .onFailure { error ->
                    _state.value = AuthState.Error(error.message ?: "Error desconocido")
                }
        }
    }

    fun resetState() {
        _state.value = AuthState.Idle
    }
}