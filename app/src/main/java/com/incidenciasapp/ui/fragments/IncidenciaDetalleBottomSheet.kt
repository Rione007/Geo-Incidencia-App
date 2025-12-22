package com.incidenciasapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.incidenciasapp.R
import com.incidenciasapp.data.remote.api.IncidenciaApiService
import com.incidenciasapp.data.remote.retrofit.RetrofitClient
import com.incidenciasapp.data.repository.IncidenciaRepository
import kotlinx.coroutines.launch

class IncidenciaDetalleBottomSheet : BottomSheetDialogFragment() {

    private val repository by lazy {
        IncidenciaRepository(
            RetrofitClient.create(IncidenciaApiService::class.java)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.bottom_sheet_incidencia_detalle, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val tvDescripcion = view.findViewById<TextView>(R.id.tvDescripcion)
        val tvDireccion = view.findViewById<TextView>(R.id.tvDireccion)
        val tvTipo = view.findViewById<TextView>(R.id.tvTipo)
        val tvSubtipo = view.findViewById<TextView>(R.id.tvSubtipo)
        val tvFecha = view.findViewById<TextView>(R.id.tvFecha)
        val tvCoordenadas = view.findViewById<TextView>(R.id.tvCoordenadas)

        val idIncidencia = requireArguments().getInt("id")

        lifecycleScope.launch {
            repository.obtenerIncidenciaPorId(idIncidencia)
                .onSuccess { data ->

                    tvDescripcion.text =
                        data.descripcion ?: "Sin descripción"

                    tvDireccion.text =
                        data.direccionReferencia ?: "Sin dirección"

                    tvFecha.text =
                        data.fechaIncidencia
                            ?.replace("T", " ")
                            ?.take(16)
                            ?: "Sin fecha"

                    tvCoordenadas.text =
                        "Lat: ${data.latitud}, Lng: ${data.longitud}"

                    // 🔹 Por ahora solo mostramos los IDs
                    tvTipo.text = "Tipo ID: ${data.idTipo}"
                    tvSubtipo.text = "Subtipo ID: ${data.idSubtipo}"
                }
                .onFailure {
                    tvDescripcion.text = "Error al cargar detalle"
                }
        }
    }

    companion object {
        fun newInstance(id: Int) =
            IncidenciaDetalleBottomSheet().apply {
                arguments = bundleOf("id" to id)
            }
    }
}
