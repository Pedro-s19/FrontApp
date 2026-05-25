package com.example.finalpro.Ui1.Screens.Logros

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class Logro(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val icono: String,
    val puntos: Int,
    val desbloqueado: Boolean
)

@HiltViewModel
class LogrosViewModel @Inject constructor() : ViewModel() {


    private val _logros = MutableStateFlow<List<Logro>>(emptyList())
    val logros: StateFlow<List<Logro>> = _logros.asStateFlow()

    private val _puntos = MutableStateFlow(0)
    val puntos: StateFlow<Int> = _puntos.asStateFlow()

    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        cargar()
    }

    fun cargar() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                _logros.value = listOf(
                    Logro("1", "Primer Gasto", "Registra tu primer gasto", "💰", 10, true),
                    Logro("2", "Ahorrador", "Ahorra 100€", "🏦", 20, false)
                )
                _puntos.value = 10
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al cargar logros"
            } finally {
                _loading.value = false
            }
        }
    }
}