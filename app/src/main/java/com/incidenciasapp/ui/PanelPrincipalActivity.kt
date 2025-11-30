package com.incidenciasapp.ui

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.incidenciasapp.R
import com.incidenciasapp.model.PanelButton
import com.incidenciasapp.ui.fragments.ExplorarFragment
import com.incidenciasapp.ui.fragments.IncidentesFragment
import com.incidenciasapp.ui.fragments.MisReportesFragment
import com.incidenciasapp.ui.fragments.PerfilFragment

class PanelPrincipalActivity : AppCompatActivity() {

    private lateinit var btnExplorar: LinearLayout
    private lateinit var btnIncidentes: LinearLayout
    private lateinit var btnMisReportes: LinearLayout
    private lateinit var btnPerfil: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_panel_principal)

        btnExplorar = findViewById(R.id.btnExplorar)
        btnIncidentes = findViewById(R.id.btnIncidentes)
        btnMisReportes = findViewById(R.id.btnMisReportes)
        btnPerfil = findViewById(R.id.btnPerfil)

        val buttons = listOf(
            PanelButton(btnExplorar, ExplorarFragment()),
            PanelButton(btnIncidentes, IncidentesFragment()),
            PanelButton(btnMisReportes, MisReportesFragment()),
            PanelButton(btnPerfil, PerfilFragment())
        )

        loadFragment(ExplorarFragment())
        setActiveButton(btnExplorar)

        buttons.forEach { button ->
            button.container.setOnClickListener {
                val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
                if (currentFragment?.javaClass == button.fragment.javaClass) return@setOnClickListener
                loadFragment(button.fragment)
                setActiveButton(button.container)
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun setActiveButton(activeButton: LinearLayout) {
        val allButtons = listOf(btnExplorar, btnIncidentes, btnMisReportes, btnPerfil)
        allButtons.forEach { button ->
            val isActive = button == activeButton
            button.isSelected = isActive
            val iconView = button.getChildAt(0) as? ImageView
            val textView = button.getChildAt(1) as? TextView
            if (isActive) {
                iconView?.setColorFilter(getColor(R.color.color_active_text))
                textView?.setTextColor(getColor(R.color.color_active_text))
            } else {
                iconView?.setColorFilter(getColor(R.color.color_inactive_text))
                textView?.setTextColor(getColor(R.color.color_inactive_text))
            }
        }
    }
}
