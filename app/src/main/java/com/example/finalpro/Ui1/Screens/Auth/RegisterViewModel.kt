package com.example.finalpro.Ui1.Screens.Auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalpro.Data.Remote.Dto.Request.RegistroRequest
import com.example.finalpro.Data.Repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class RegisterState {
    object Idle    : RegisterState()
    object Loading : RegisterState()
    object Success : RegisterState()
    data class Error(val msg: String) : RegisterState()
}

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val state: StateFlow<RegisterState> = _state.asStateFlow()

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _state.value = RegisterState.Loading
            authRepository.register(RegistroRequest(email, password))
                .onSuccess  { _state.value = RegisterState.Success }
                .onFailure  { _state.value = RegisterState.Error(it.message ?: "Error al registrar") }
        }
    }

    fun resetState() {
        _state.value = RegisterState.Idle
    }
}