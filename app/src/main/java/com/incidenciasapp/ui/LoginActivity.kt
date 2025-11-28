package com.incidenciasapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.incidenciasapp.R
import com.incidenciasapp.data.local.UserPreferences
import com.incidenciasapp.data.remote.api.AuthApiService
import com.incidenciasapp.data.remote.retrofit.RetrofitClient
import com.incidenciasapp.data.repository.AuthRepository
import com.incidenciasapp.ui.viewmodel.AuthViewModel
import com.incidenciasapp.ui.viewmodel.AuthViewModelFactory
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {

    private lateinit var userPrefs: UserPreferences

    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        userPrefs = UserPreferences(this)


        val authApi = RetrofitClient.create(AuthApiService::class.java) { null }
        val repo = AuthRepository(authApi)
        val factory = AuthViewModelFactory(repo)

        viewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]

        val user = findViewById<EditText>(R.id.txtEmail)
        val pass = findViewById<EditText>(R.id.txtPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            viewModel.login(user.text.toString(), pass.text.toString())
        }

        viewModel.loginResult.observe(this) { response ->
            // Login OK
            Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show()

            // Guardar token
            lifecycleScope.launch {

                // Guardar token
                userPrefs.saveToken(response.usuario.TOKEN)

                // Guardar datos del usuario
                userPrefs.saveUser(
                    name = response.usuario.NOMBRE,
                    email = response.usuario.EMAIL,
                    role = response.usuario.ROL
                )
            }

            // Ir al Home
            startActivity(Intent(this, PanelPrincipalActivity::class.java))
            finish()
        }

    }
}
