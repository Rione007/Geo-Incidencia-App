package com.incidenciasapp.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.incidenciasapp.R
import com.incidenciasapp.data.repository.AuthRepository
import com.incidenciasapp.ui.viewmodel.RegistrarViewModel
import com.incidenciasapp.ui.viewmodel.ViewModelFactory
import androidx.lifecycle.lifecycleScope
import com.incidenciasapp.data.remote.api.AuthApiService
import com.incidenciasapp.data.remote.retrofit.RetrofitClient
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RegistrarActivity : AppCompatActivity() {

    private lateinit var viewModel: RegistrarViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar)

        val nombre = findViewById<EditText>(R.id.txtNombre)
        val email = findViewById<EditText>(R.id.txtEmail)
        val password = findViewById<EditText>(R.id.txtPassword)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)

        // Inicializar ViewModel
        val api = RetrofitClient.create(AuthApiService::class.java)
        val repo = AuthRepository(api)
        val factory = ViewModelFactory(repo)
        viewModel = ViewModelProvider(this, factory)[RegistrarViewModel::class.java]

        // Botón registrar
        btnRegistrar.setOnClickListener {
            val nom = nombre.text.toString()
            val ema = email.text.toString()
            val pass = password.text.toString()

            if (nom.isEmpty() || ema.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.registrar(nom, ema, pass)
        }

        // Observadores del estado
        lifecycleScope.launch {
            viewModel.loading.collectLatest { loading ->
                if (loading) {
                    Toast.makeText(this@RegistrarActivity, "Registrando...", Toast.LENGTH_SHORT).show()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.resultado.collectLatest { resp ->
                if (resp != null) {
                    Toast.makeText(this@RegistrarActivity, resp.mensaje, Toast.LENGTH_LONG).show()
                    finish() // cerrar pantalla y volver al login
                }
            }
        }

        lifecycleScope.launch {
            viewModel.error.collectLatest { error ->
                error?.let {
                    Toast.makeText(this@RegistrarActivity, it, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
