package com.incidenciasapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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
import com.incidenciasapp.ui.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {

    private lateinit var userPrefs: UserPreferences
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userPrefs = UserPreferences(this)
        checkUserSession()
        setContentView(R.layout.activity_login)



        val authApi = RetrofitClient.create(AuthApiService::class.java) { null }
        val repo = AuthRepository(authApi)
        val factory = ViewModelFactory(repo)
        viewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]

        val user = findViewById<EditText>(R.id.txtEmail)
        val pass = findViewById<EditText>(R.id.txtPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegistrar = findViewById<TextView>(R.id.sign_up)

        btnLogin.setOnClickListener {
            viewModel.login(user.text.toString(), pass.text.toString())
        }

        btnRegistrar.setOnClickListener {
            startActivity(Intent(this, RegistrarActivity::class.java))
        }

        viewModel.loginResult.observe(this) { response ->
            if (!response.exito) {
                Toast.makeText(this, "Error: ${response.mensaje}", Toast.LENGTH_LONG).show()
                return@observe
            }

            val token = response.usuario.TOKEN
            if (token.isNullOrBlank()) {
                Toast.makeText(this, "Error: No se recibió token del servidor", Toast.LENGTH_LONG).show()
                return@observe
            }

            Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show()

            lifecycleScope.launch {
                userPrefs.saveToken(token)
                userPrefs.saveUserId(response.usuario.ID_USUARIO)
                userPrefs.saveUser(
                    name = response.usuario.NOMBRE,
                    email = response.usuario.EMAIL,
                    role = response.usuario.ROL
                )

                val savedToken = userPrefs.getToken().first()
                val savedUserId = userPrefs.getUserId().first()

                if (savedToken == token && savedUserId == response.usuario.ID_USUARIO) {
                    startActivity(Intent(this@LoginActivity, PanelPrincipalActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Error al guardar sesión", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun checkUserSession() {
        lifecycleScope.launch {
            // Leemos el primer valor que emita el Flow del token
            val token = userPrefs.getToken().first()

            if (!token.isNullOrBlank()) {
                // Si el token existe, saltamos al Panel Principal
                startActivity(Intent(this@LoginActivity, PanelPrincipalActivity::class.java))
                finish() // Importante para que no puedan volver atrás al login
            }
        }
    }
}