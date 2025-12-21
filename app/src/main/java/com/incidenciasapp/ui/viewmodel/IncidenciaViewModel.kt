package com.incidenciasapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.incidenciasapp.data.repository.IncidenciaRepository
import com.incidenciasapp.dto.incidencia.IncidenciaListadoDto
import com.incidenciasapp.dto.incidencia.IncidenciaRequest
import com.incidenciasapp.dto.incidencia.IncidenciaResponse
import kotlinx.coroutines.launch

class IncidenciaViewModel(
    private val repository: IncidenciaRepository
) : ViewModel() {

    val loading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val incidenciaResult = MutableLiveData<IncidenciaResponse>()

    val incidenciasList = MutableLiveData<List<IncidenciaListadoDto>>()

    fun crearIncidencia(request: IncidenciaRequest) {
        viewModelScope.launch {
            loading.value = true

            val result = repository.createIncidencia(request)

            loading.value = false

            result.onSuccess {
                incidenciaResult.value = it
            }.onFailure {
                errorMessage.value = it.message
            }
        }
    }
    // Función para obtener la lista
    fun cargarIncidencias(tipo: Int? = null) {
        viewModelScope.launch {
            loading.value = true
            val result = repository.listarIncidencias(tipo)
            loading.value = false

            result.onSuccess {
                incidenciasList.value = it
            }.onFailure {
                errorMessage.value = it.message
            }
        }
    }
}