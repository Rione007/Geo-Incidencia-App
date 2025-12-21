package com.incidenciasapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.incidenciasapp.data.repository.AuthRepository
import com.incidenciasapp.data.repository.IncidenciaRepository

class ViewModelFactory(
    private val repo: Any
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) ->
                AuthViewModel(repo as AuthRepository) as T

            modelClass.isAssignableFrom(RegistrarViewModel::class.java) ->
                RegistrarViewModel(repo as AuthRepository) as T

            modelClass.isAssignableFrom(IncidenciaViewModel::class.java) -> {
                IncidenciaViewModel(repo as IncidenciaRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}