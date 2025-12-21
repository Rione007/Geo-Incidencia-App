package com.incidenciasapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Layout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.google.android.material.card.MaterialCardView
import com.incidenciasapp.R
import com.incidenciasapp.data.local.UserPreferences
import com.incidenciasapp.ui.LoginActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PerfilFragment : Fragment() {

    private lateinit var userPrefs: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_perfil, container, false)

        userPrefs = UserPreferences(requireContext())

        val tvNombre = view.findViewById<TextView>(R.id.tvUserName)
        val tvCorreo = view.findViewById<TextView>(R.id.tvEmail)
        val btnCerrarSesion = view.findViewById<MaterialCardView>(R.id.layoutCerrarSesion)


        lifecycleScope.launch {
            val nombre = userPrefs.getUserName().first() ?: "Usuario"
            val email = userPrefs.getUserEmail().first() ?: "Sin correo"

            tvNombre.text = nombre
            tvCorreo.text = email
        }

        btnCerrarSesion.setOnClickListener {
            cerrarSesion()
        }

        return view
    }

    private fun cerrarSesion() {
        lifecycleScope.launch {
            userPrefs.clear()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    companion object {
        fun newInstance() : PerfilFragment = PerfilFragment()
    }
}