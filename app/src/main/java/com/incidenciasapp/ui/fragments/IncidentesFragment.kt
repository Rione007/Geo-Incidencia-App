package com.incidenciasapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.incidenciasapp.R
import com.incidenciasapp.data.remote.api.IncidenciaApiService
import com.incidenciasapp.data.remote.retrofit.RetrofitClient
import com.incidenciasapp.data.repository.IncidenciaRepository
import com.incidenciasapp.ui.adapter.IncidenciaAdapter
import com.incidenciasapp.ui.viewmodel.IncidenciaViewModel
import com.incidenciasapp.ui.viewmodel.ViewModelFactory


class IncidentesFragment : Fragment() {
    private lateinit var viewModel: IncidenciaViewModel
    private lateinit var adapter: IncidenciaAdapter // El que creamos antes
    private lateinit var rv: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_incidentes, container, false)

        // 1. Configurar la API (Igual que en LoginActivity)
        // Usamos el mismo RetrofitClient que ya tienes
        val incidenciaApi = RetrofitClient.create(IncidenciaApiService::class.java) { null }

        // 2. Crear Repositorio y Factory
        val repo = IncidenciaRepository(incidenciaApi)
        val factory = ViewModelFactory(repo)

        // 3. INICIALIZAR EL VIEWMODEL (Esto quita el crash)
        viewModel = ViewModelProvider(this, factory)[IncidenciaViewModel::class.java]

        setupRecyclerView(view)
        setupObservers()
        viewModel.cargarIncidencias()
        return view
    }
    private fun setupRecyclerView(view: View) {
        rv = view.findViewById(R.id.rvIncidencias)
        rv.layoutManager = LinearLayoutManager(requireContext())

        // Inicializamos con lista vacía
        adapter = IncidenciaAdapter(emptyList()) { incidencia ->
            Toast.makeText(context, "Detalle de: ${incidencia.descripcion}", Toast.LENGTH_SHORT).show()
        }
        rv.adapter = adapter
    }

    private fun setupObservers() {
        // Cada vez que incidenciasList cambie en el ViewModel, esto se ejecuta:
        viewModel.incidenciasList.observe(viewLifecycleOwner) { lista ->
            adapter.updateData(lista)
        }

        // Para manejar errores
        viewModel.errorMessage.observe(viewLifecycleOwner) { msg ->
            if (msg != null) Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
        }
    }

    companion object {

        fun newInstance() : IncidentesFragment =IncidentesFragment()
    }
}