package com.example.finalpro.Ui1.Screens.Ingresos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalpro.Data.Remote.Dto.Request.IngresoRequest
import com.example.finalpro.Data.Remote.Dto.Response.IngresoResponse
import com.example.finalpro.Data.Repository.IngresoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IngresosViewModel @Inject constructor(
    private val ingresoRepository: IngresoRepository
) : ViewModel() {
    private val _ingresos = MutableStateFlow<List<IngresoResponse>>(emptyList())
    val ingresos: StateFlow<List<IngresoResponse>> = _ingresos.asStateFlow()
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    init { cargarIngresos() }

    fun cargarIngresos() {
        viewModelScope.launch {
            _loading.value = true
            ingresoRepository.listar().onSuccess { _ingresos.value = it }
            _loading.value = false
        }
    }

    fun crearIngreso(monto: Double, descripcion: String, fecha: String) {
        viewModelScope.launch {
            ingresoRepository.crear(IngresoRequest(monto, descripcion, fecha)).onSuccess { cargarIngresos() }
        }
    }

    fun actualizarIngreso(id: String, monto: Double, descripcion: String, fecha: String) {
        viewModelScope.launch {
            ingresoRepository.actualizar(id, IngresoRequest(monto, descripcion, fecha))
                .onSuccess { cargarIngresos() }
        }
    }

    fun eliminarIngreso(id: String) {
        viewModelScope.launch { ingresoRepository.eliminar(id).onSuccess { cargarIngresos() } }
    }
}