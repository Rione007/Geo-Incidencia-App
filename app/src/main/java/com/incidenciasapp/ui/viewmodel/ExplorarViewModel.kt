package com.incidenciasapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.incidenciasapp.data.repository.IncidenciaRepository
import com.incidenciasapp.dto.incidencia.IncidenciaAreaRequest
import com.incidenciasapp.dto.incidencia.IncidenciaDto
import kotlinx.coroutines.launch

class ExplorarViewModel(
    private val repository: IncidenciaRepository
) : ViewModel() {

    val incidenciasMapa = MutableLiveData<List<IncidenciaDto>>()
    val errorMessage = MutableLiveData<String?>()

    fun cargarPorArea(request: IncidenciaAreaRequest) {
        viewModelScope.launch {
            repository.buscarIncidenciasPorArea(request)
                .onSuccess { incidenciasMapa.value = it }
                .onFailure { errorMessage.value = it.message }
        }
    }
}
