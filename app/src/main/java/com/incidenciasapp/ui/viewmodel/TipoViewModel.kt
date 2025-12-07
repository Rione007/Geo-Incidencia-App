package com.incidenciasapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.incidenciasapp.data.repository.TipoRepository
import com.incidenciasapp.dto.Tipo.TipoItem
import kotlinx.coroutines.launch

class TipoViewModel(
    private val repository: TipoRepository
) : ViewModel() {

    private val _tipos = MutableLiveData<List<TipoItem>>(emptyList())
    val tipos: LiveData<List<TipoItem>> = _tipos

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    fun cargarTipos() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val result = repository.obtenerTipos()
                if (result.isSuccess) {
                    _tipos.value = result.getOrNull() ?: emptyList()
                    _error.value = null
                } else {
                    _error.value = result.exceptionOrNull()?.message ?: "Error desconocido"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al cargar tipos"
            } finally {
                _loading.value = false
            }
        }
    }
}