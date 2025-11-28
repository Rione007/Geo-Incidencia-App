package com.incidenciasapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.incidenciasapp.data.repository.AuthRepository
import com.incidenciasapp.dto.login.LoginResponse
import kotlinx.coroutines.launch

class AuthViewModel(private val repo: AuthRepository) : ViewModel() {

    val loading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val loginResult = MutableLiveData<LoginResponse>()

    fun login(email: String, contrasena: String) {
        viewModelScope.launch {
            loading.value = true

            val result = repo.login(email, contrasena)

            loading.value = false

            result.onSuccess {
                loginResult.value = it
            }.onFailure {
                errorMessage.value = it.message
            }
        }
    }
}
