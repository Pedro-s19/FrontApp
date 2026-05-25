package com.example.finalpro.Ui1.Screens.Recurrentes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalpro.Data.Remote.Dto.Request.GastoRecurrenteRequest
import com.example.finalpro.Data.Remote.Dto.Response.CategoriaResponse
import com.example.finalpro.Data.Remote.Dto.Response.GastoRecurrenteResponse
import com.example.finalpro.Data.Repository.CategoriaRepository
import com.example.finalpro.Data.Repository.GastoRecurrenteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecurrentesViewModel @Inject constructor(
    private val repository: GastoRecurrenteRepository,
    private val categoriaRepository: CategoriaRepository
) : ViewModel() {

    private val _recurrentes = MutableStateFlow<List<GastoRecurrenteResponse>>(emptyList())
    val recurrentes: StateFlow<List<GastoRecurrenteResponse>> = _recurrentes.asStateFlow()

    private val _categorias = MutableStateFlow<List<CategoriaResponse>>(emptyList())
    val categorias: StateFlow<List<CategoriaResponse>> = _categorias.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    init { cargar() }

    fun cargar() {
        viewModelScope.launch {
            _loading.value = true
            repository.listar().onSuccess { _recurrentes.value = it }
            categoriaRepository.listar().onSuccess { _categorias.value = it }
            _loading.value = false
        }
    }

    fun crear(nombre: String, monto: Double, diaMes: Int, categoriaId: String) {
        viewModelScope.launch {
            repository.crear(GastoRecurrenteRequest(nombre, monto, diaMes, categoriaId))
                .onSuccess { cargar() }
        }
    }

    fun actualizar(id: String, nombre: String, monto: Double, diaMes: Int, categoriaId: String) {
        viewModelScope.launch {
            repository.actualizar(id, GastoRecurrenteRequest(nombre, monto, diaMes, categoriaId))
                .onSuccess { cargar() }
        }
    }

    fun eliminar(id: String) {
        viewModelScope.launch {
            repository.eliminar(id).onSuccess { cargar() }
        }
    }
}