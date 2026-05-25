package com.example.finalpro.Ui1.Screens.Comparativas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalpro.Data.Local.SessionManager
import com.example.finalpro.Data.Remote.Dto.Response.PresupuestoResponse
import com.example.finalpro.Data.Repository.PresupuestoRepository
import com.example.finalpro.Data.Repository.ReporteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ComparativasViewModel @Inject constructor(
    private val reporteRepository: ReporteRepository,
    private val presupuestoRepository: PresupuestoRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    // Mapa: "MM/AAAA" -> (Ingresos, Gastos)
    private val _ingresosVsGastos = MutableStateFlow<Map<String, Pair<Double, Double>>>(emptyMap())
    val ingresosVsGastos: StateFlow<Map<String, Pair<Double, Double>>> = _ingresosVsGastos.asStateFlow()

    private val _presupuestos = MutableStateFlow<List<PresupuestoResponse>>(emptyList())
    val presupuestos: StateFlow<List<PresupuestoResponse>> = _presupuestos.asStateFlow()

    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    init { cargarDatos() }

    fun cargarDatos() {
        viewModelScope.launch {
            _loading.value = true
            val moneda = sessionManager.getMoneda().first()
            val hoy = LocalDate.now()

            // ---------- Ingresos vs Gastos (últimos 6 meses) ----------
            val meses = (5 downTo 0).map { hoy.minusMonths(it.toLong()) }
            val mapa = linkedMapOf<String, Pair<Double, Double>>()  // para mantener orden
            for (fecha in meses) {
                val res = reporteRepository.resumenMensual(fecha.year, fecha.monthValue, moneda).getOrNull()
                val etiqueta = "${fecha.monthValue.toString().padStart(2, '0')}/${fecha.year}"
                mapa[etiqueta] = Pair(res?.totalIngresos ?: 0.0, res?.totalGastos ?: 0.0)
            }
            _ingresosVsGastos.value = mapa

            // ---------- Presupuestos del mes actual ----------
            presupuestoRepository.listar(hoy.year, hoy.monthValue, moneda)
                .onSuccess { _presupuestos.value = it }
                .onFailure { _presupuestos.value = emptyList() }

            _loading.value = false
        }
    }
}