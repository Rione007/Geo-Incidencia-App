package com.incidenciasapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.incidenciasapp.data.repository.SubtipoRepository
import com.incidenciasapp.dto.Subtipo.SubtipoItem
import kotlinx.coroutines.launch

class SubtipoViewModel(
    private val repository: SubtipoRepository
) : ViewModel() {

    private val _subtipos = MutableLiveData<List<SubtipoItem>>(emptyList())
    val subtipos: LiveData<List<SubtipoItem>> = _subtipos

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    fun cargarSubtipos() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val result = repository.obtenerSubtipos()
                if (result.isSuccess) {
                    _subtipos.value = result.getOrNull() ?: emptyList()
                    _error.value = null
                } else {
                    _error.value = result.exceptionOrNull()?.message ?: "Error desconocido"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al cargar subtipos"
            } finally {
                _loading.value = false
            }
        }
    }
}