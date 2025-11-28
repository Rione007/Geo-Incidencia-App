package com.incidenciasapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.incidenciasapp.data.repository.AuthRepository

class ViewModelFactory(
    private val repo: AuthRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) ->
                AuthViewModel(repo) as T

            modelClass.isAssignableFrom(RegistrarViewModel::class.java) ->
                RegistrarViewModel(repo) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}