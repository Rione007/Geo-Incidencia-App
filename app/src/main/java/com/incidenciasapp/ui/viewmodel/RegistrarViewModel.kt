package com.incidenciasapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.incidenciasapp.data.repository.AuthRepository
import com.incidenciasapp.dto.registrar.RegistrarResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegistrarViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _resultado = MutableStateFlow<RegistrarResponse?>(null)
    val resultado: StateFlow<RegistrarResponse?> = _resultado

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error


    fun registrar(nombre: String, email: String, contrasena: String) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.registrar(nombre, email, contrasena)

            if (result.isSuccess) {
                _resultado.value = result.getOrNull()
            } else {
                _error.value = result.exceptionOrNull()?.message ?: "Error desconocido"
            }

            _loading.value = false
        }
    }
}
