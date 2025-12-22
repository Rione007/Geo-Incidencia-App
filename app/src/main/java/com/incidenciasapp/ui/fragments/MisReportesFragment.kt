package com.incidenciasapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.incidenciasapp.R
import com.incidenciasapp.data.local.UserPreferences
import com.incidenciasapp.data.remote.api.IncidenciaApiService
import com.incidenciasapp.data.remote.retrofit.RetrofitClient
import com.incidenciasapp.data.repository.IncidenciaRepository
import com.incidenciasapp.dto.incidencia.IncidenciaListadoDto
import com.incidenciasapp.ui.adapter.IncidenciaAdapter
import com.incidenciasapp.ui.viewmodel.IncidenciaViewModel
import com.incidenciasapp.ui.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MisReportesFragment : Fragment() {

    private lateinit var viewModel: IncidenciaViewModel
    private lateinit var adapter: IncidenciaAdapter
    private lateinit var rv: RecyclerView
    private lateinit var etSearch: EditText

    private var listaCompleta: List<IncidenciaListadoDto> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_incidentes, container, false)

        // API + Repo + ViewModel
        val api = RetrofitClient.create(IncidenciaApiService::class.java)
        val repo = IncidenciaRepository(api)
        val factory = ViewModelFactory(repo)
        viewModel = ViewModelProvider(this, factory)[IncidenciaViewModel::class.java]

        etSearch = view.findViewById(R.id.etSearch)
        setupRecyclerView(view)
        setupObservers()
        setupSearch()

        cargarMisIncidencias()

        val fabAdd = view.findViewById<FloatingActionButton>(R.id.fabAdd)
        fabAdd.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, NuevaIncidenciaFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    // -----------------------------
    // RecyclerView
    // -----------------------------
    private fun setupRecyclerView(view: View) {
        rv = view.findViewById(R.id.rvIncidencias)
        rv.layoutManager = LinearLayoutManager(requireContext())

        adapter = IncidenciaAdapter(emptyList()) { incidencia ->
            mostrarDetalle(incidencia.idIncidencia)
        }
        rv.adapter = adapter
    }

    // -----------------------------
    // Observers
    // -----------------------------
    private fun setupObservers() {
        viewModel.incidenciasList.observe(viewLifecycleOwner) { lista ->
            listaCompleta = lista
            adapter.updateData(lista)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { msg ->
            msg?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }

    // -----------------------------
    // Search
    // -----------------------------
    private fun setupSearch() {
        etSearch.addTextChangedListener { text ->
            val query = text.toString().lowercase()

            val filtrada = listaCompleta.filter {
                it.descripcion?.lowercase()?.contains(query) == true
            }

            adapter.updateData(filtrada)
        }
    }

    // -----------------------------
    // Load user's incidences
    // -----------------------------
    private fun cargarMisIncidencias() {
        val userId = runBlocking {
            UserPreferences(requireContext()).getUserId().first()
        }

        if (userId == null) {
            Toast.makeText(requireContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.cargarIncidenciasPorUsuario(userId)
    }

    // -----------------------------
    // BottomSheet
    // -----------------------------
    private fun mostrarDetalle(idIncidencia: Int) {
        IncidenciaDetalleBottomSheet
            .newInstance(idIncidencia)
            .show(parentFragmentManager, "IncidenciaDetalle")
    }
}
