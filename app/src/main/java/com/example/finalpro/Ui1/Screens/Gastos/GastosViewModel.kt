package com.example.finalpro.Ui1.Screens.Gastos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalpro.Data.Remote.Dto.Request.GastoRequest
import com.example.finalpro.Data.Remote.Dto.Response.CategoriaResponse
import com.example.finalpro.Data.Remote.Dto.Response.GastoResponse
import com.example.finalpro.Data.Remote.Dto.Response.LogroResponse
import com.example.finalpro.Data.Repository.AlertaRepository
import com.example.finalpro.Data.Repository.CategoriaRepository
import com.example.finalpro.Data.Repository.GamificacionRepository
import com.example.finalpro.Data.Repository.GastoRepository
import com.example.finalpro.Data.Repository.ReporteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class GastosViewModel @Inject constructor(
    private val gastoRepository: GastoRepository,
    private val categoriaRepository: CategoriaRepository,
    private val reporteRepository: ReporteRepository,
    private val alertaRepository: AlertaRepository,
    private val gamificacionRepository: GamificacionRepository
) : ViewModel() {

    private val _gastos = MutableStateFlow<List<GastoResponse>>(emptyList())
    val gastos: StateFlow<List<GastoResponse>> = _gastos.asStateFlow()

    private val _categorias = MutableStateFlow<List<CategoriaResponse>>(emptyList())
    val categorias: StateFlow<List<CategoriaResponse>> = _categorias.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()


    private val _saldoDisponible = MutableStateFlow(0.0)
    val saldoDisponible: StateFlow<Double> = _saldoDisponible.asStateFlow()

    private val _alertas = MutableStateFlow<List<String>>(emptyList())
    val alertas: StateFlow<List<String>> = _alertas.asStateFlow()


    private val _alertasActivas = MutableStateFlow<List<String>>(emptyList())
    val alertasActivas: StateFlow<List<String>> = _alertasActivas.asStateFlow()


    private val _logroNuevo = MutableStateFlow<LogroResponse?>(null)
    val logroNuevo: StateFlow<LogroResponse?> = _logroNuevo.asStateFlow()


    private var logrosAnteriores = setOf<String>()

    init { cargarDatos() }

    fun cargarDatos() {
        viewModelScope.launch {
            _loading.value = true
            gastoRepository.listar().onSuccess { _gastos.value = it }
            categoriaRepository.listar().onSuccess { _categorias.value = it }
            val hoy = LocalDate.now()
            reporteRepository.resumenMensual(hoy.year, hoy.monthValue)
                .onSuccess { _saldoDisponible.value = it.balance.coerceAtLeast(0.0) }
                .onFailure { _saldoDisponible.value = Double.MAX_VALUE }
            alertaRepository.obtenerAlertas()
                .onSuccess { _alertas.value = it.alertas }
                .onFailure { _alertas.value = emptyList() }
            // Guardar snapshot de logros para detectar nuevos
            gamificacionRepository.listarLogros()
                .onSuccess { logrosAnteriores = it.filter { l -> l.desbloqueado }.map { l -> l.id }.toSet() }
            _loading.value = false
        }
    }
    fun verificarAlertas(): Boolean {
        val activas = _alertas.value
        return if (activas.isNotEmpty()) {
            _alertasActivas.value = activas
            true
        } else {
            false
        }
    }

    fun dismissAlertas() {
        _alertasActivas.value = emptyList()
    }

    fun dismissLogro() {
        _logroNuevo.value = null
    }

    fun crearGasto(monto: Double, descripcion: String, fecha: String, categoriaId: String) {
        viewModelScope.launch {
            gastoRepository.crear(GastoRequest(monto, descripcion, fecha, categoriaId))
                .onSuccess {
                    cargarDatos()
                    verificarLogrosNuevos()
                }
        }
    }

    fun actualizarGasto(id: String, monto: Double, descripcion: String, fecha: String) {
        viewModelScope.launch {
            val categoriaId = _gastos.value.find { it.id == id }?.categoria?.id ?: return@launch
            gastoRepository.actualizar(id, GastoRequest(monto, descripcion, fecha, categoriaId))
                .onSuccess { cargarDatos() }
        }
    }

    fun eliminarGasto(id: String) {
        viewModelScope.launch { gastoRepository.eliminar(id).onSuccess { cargarDatos() } }
    }

    private suspend fun verificarLogrosNuevos() {
        gamificacionRepository.listarLogros().onSuccess { nuevosLogros ->
            val desbloqueadosAhora = nuevosLogros.filter { it.desbloqueado }.map { it.id }.toSet()
            val recienDesbloqueados = desbloqueadosAhora - logrosAnteriores
            if (recienDesbloqueados.isNotEmpty()) {
                val logro = nuevosLogros.first { it.id in recienDesbloqueados }
                _logroNuevo.value = logro
            }
            logrosAnteriores = desbloqueadosAhora
        }
    }
}