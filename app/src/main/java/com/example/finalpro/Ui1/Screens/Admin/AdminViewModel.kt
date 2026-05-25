package com.example.finalpro.Ui1.Screens.Admin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalpro.Data.Remote.Dto.Response.UsuarioResponse
import com.example.finalpro.Data.Repository.AdminRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val adminRepository: AdminRepository
) : ViewModel() {

    private val _usuarios = MutableStateFlow<List<UsuarioResponse>>(emptyList())
    val usuarios: StateFlow<List<UsuarioResponse>> = _usuarios.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init { cargarUsuarios() }

    fun cargarUsuarios() {
        viewModelScope.launch {
            _loading.value = true
            adminRepository.listarUsuarios()
                .onSuccess { _usuarios.value = it.filter{ usr -> usr.activo}; _error.value = null }
                .onFailure { e ->
                    Log.e("AdminVM", "Error al cargar usuarios", e)
                    _error.value = e.message ?: "Error desconocido"
                }
            _loading.value = false
        }
    }

    fun eliminarUsuario(id: String) {
        viewModelScope.launch {
            adminRepository.eliminarUsuario(id)
                .onSuccess { cargarUsuarios() }
                .onFailure { _error.value = it.message }
        }
    }
}