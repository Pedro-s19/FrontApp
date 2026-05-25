package com.example.finalpro.Ui1.Screens.Presupuesto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalpro.Data.Remote.Dto.Request.PresupuestoRequest
import com.example.finalpro.Data.Remote.Dto.Response.CategoriaResponse
import com.example.finalpro.Data.Remote.Dto.Response.PresupuestoResponse
import com.example.finalpro.Data.Repository.AlertaRepository
import com.example.finalpro.Data.Repository.CategoriaRepository
import com.example.finalpro.Data.Repository.PresupuestoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class PresupuestoViewModel @Inject constructor(
    private val presupuestoRepository: PresupuestoRepository,
    private val categoriaRepository: CategoriaRepository,
    private val alertaRepository: AlertaRepository
) : ViewModel() {

    private val _presupuestos = MutableStateFlow<List<PresupuestoResponse>>(emptyList())
    val presupuestos: StateFlow<List<PresupuestoResponse>> = _presupuestos.asStateFlow()

    private val _categorias = MutableStateFlow<List<CategoriaResponse>>(emptyList())
    val categorias: StateFlow<List<CategoriaResponse>> = _categorias.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _alertas = MutableStateFlow<List<String>>(emptyList())
    val alertas: StateFlow<List<String>> = _alertas.asStateFlow()

    private val anioActual = LocalDate.now().year
    private val mesActual  = LocalDate.now().monthValue

    init { cargarDatos() }

    fun cargarDatos() {
        viewModelScope.launch {
            _loading.value = true
            presupuestoRepository.listar(anioActual, mesActual)
                .onSuccess { _presupuestos.value = it }
            categoriaRepository.listar()
                .onSuccess { _categorias.value = it }
            alertaRepository.obtenerAlertas()
                .onSuccess { _alertas.value = it.alertas }
            _loading.value = false
        }
    }

    fun crearPresupuesto(categoriaId: String, limiteMonto: Double) {
        viewModelScope.launch {
            presupuestoRepository.crear(
                PresupuestoRequest(categoriaId, anioActual, mesActual, limiteMonto)
            ).onSuccess { cargarDatos() }
        }
    }

    fun actualizarPresupuesto(id: String, limiteMonto: Double) {
        viewModelScope.launch {
            val p = _presupuestos.value.find { it.id == id } ?: return@launch
            presupuestoRepository.actualizar(
                id,
                PresupuestoRequest(p.categoriaId, p.anio, p.mes, limiteMonto)
            ).onSuccess { cargarDatos() }
        }
    }

    fun eliminarPresupuesto(id: String) {
        viewModelScope.launch {
            presupuestoRepository.eliminar(id)
                .onSuccess { cargarDatos() }
        }
    }
}