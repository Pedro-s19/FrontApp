package com.example.finalpro.Ui1.Screens.Reportes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalpro.Data.Remote.Dto.Response.DistribucionCategoriaResponse
import com.example.finalpro.Data.Remote.Dto.Response.ResumenMensualResponse
import com.example.finalpro.Data.Remote.Dto.Response.TendenciaResponse
import com.example.finalpro.Data.Repository.ReporteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ReportesViewModel @Inject constructor(
    private val reporteRepository: ReporteRepository
) : ViewModel() {

    private val _tendencia = MutableStateFlow<TendenciaResponse?>(null)
    val tendencia: StateFlow<TendenciaResponse?> = _tendencia.asStateFlow()

    private val _distribucion = MutableStateFlow<DistribucionCategoriaResponse?>(null)
    val distribucion: StateFlow<DistribucionCategoriaResponse?> = _distribucion.asStateFlow()

    private val _resumen = MutableStateFlow<ResumenMensualResponse?>(null)
    val resumen: StateFlow<ResumenMensualResponse?> = _resumen.asStateFlow()

    private val _gastosDiarios = MutableStateFlow<Map<LocalDate, Double>>(emptyMap())
    val gastosDiarios: StateFlow<Map<LocalDate, Double>> = _gastosDiarios.asStateFlow()

    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    var anio: Int = LocalDate.now().year
        private set
    var mes: Int = LocalDate.now().monthValue
        private set
    var moneda: String = "COP"
        private set

    fun cargar(anio: Int = this.anio, mes: Int = this.mes, moneda: String = this.moneda) {
        this.anio = anio
        this.mes = mes
        this.moneda = moneda
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            val mensajeError = "No se pudo conectar al servidor. Puede estar iniciando, intenta de nuevo en unos segundos."

            reporteRepository.tendenciaDiaria(anio, mes, moneda)
                .onSuccess { _tendencia.value = it }
                .onFailure { _error.value = mensajeError }

            reporteRepository.distribucionCategorias(anio, mes, moneda)
                .onSuccess { _distribucion.value = it }
                .onFailure { if (_error.value == null) _error.value = mensajeError }

            reporteRepository.resumenMensual(anio, mes, moneda)
                .onSuccess { _resumen.value = it }
                .onFailure { if (_error.value == null) _error.value = mensajeError }

            reporteRepository.gastosDiarios(anio, mes, moneda)
                .onSuccess { _gastosDiarios.value = it }
                .onFailure { if (_error.value == null) _error.value = mensajeError }

            _loading.value = false
        }
    }
}