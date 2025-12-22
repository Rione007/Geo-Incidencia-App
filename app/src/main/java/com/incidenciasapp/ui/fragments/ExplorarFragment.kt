package com.incidenciasapp.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.incidenciasapp.R
import com.incidenciasapp.data.remote.api.IncidenciaApiService
import com.incidenciasapp.data.remote.retrofit.RetrofitClient
import com.incidenciasapp.data.repository.IncidenciaRepository
import com.incidenciasapp.dto.incidencia.IncidenciaAreaRequest
import com.incidenciasapp.ui.viewmodel.ExplorarViewModel
import com.incidenciasapp.ui.viewmodel.ViewModelFactory


class ExplorarFragment : Fragment() {

    private var mapView: MapView? = null
    private lateinit var googleMap: GoogleMap
    private lateinit var viewModel: ExplorarViewModel

    private var lastRequestTime = 0L

    private var markerSeleccionado = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_explorar, container, false)

        mapView = root.findViewById(R.id.mapView)
        mapView?.onCreate(savedInstanceState)

        // --- ViewModel ---
        val api = RetrofitClient.create(IncidenciaApiService::class.java)
        val repo = IncidenciaRepository(api)
        val factory = ViewModelFactory(repo)
        viewModel = ViewModelProvider(this, factory)[ExplorarViewModel::class.java]

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView?.getMapAsync { map ->
            MapsInitializer.initialize(requireContext())
            googleMap = map

            initMap()
            observeViewModel()
        }
    }

    @SuppressLint("PotentialBehaviorOverride")
    private fun initMap() {

        val trujillo = LatLng(-8.1091, -79.0215)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(trujillo, 12f))

        googleMap.setOnCameraIdleListener {

            if (markerSeleccionado) return@setOnCameraIdleListener

            val now = System.currentTimeMillis()
            if (now - lastRequestTime < 800) return@setOnCameraIdleListener
            lastRequestTime = now

            val bounds = googleMap.projection.visibleRegion.latLngBounds

            val request = IncidenciaAreaRequest(
                minLat = bounds.southwest.latitude,
                maxLat = bounds.northeast.latitude,
                minLng = bounds.southwest.longitude,
                maxLng = bounds.northeast.longitude,
                tipos = null,
                subtipos = null,
                dias = 7
            )

            viewModel.cargarPorArea(request)
        }

        googleMap.setOnMarkerClickListener { marker ->
            markerSeleccionado = true
            googleMap.animateCamera(
                CameraUpdateFactory.newLatLng(marker.position)
            )
            marker.showInfoWindow()
            true
        }

        googleMap.setOnInfoWindowCloseListener {
            markerSeleccionado = false
        }

        googleMap.setOnMapClickListener {
            markerSeleccionado = false
        }
    }


    private fun observeViewModel() {
        viewModel.incidenciasMapa.observe(viewLifecycleOwner) { lista ->
            googleMap.clear()

            lista.forEach { incidencia ->
                googleMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(incidencia.latitud, incidencia.longitud))
                        .title(incidencia.descripcion ?: "Incidencia")
                        .icon(
                            BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_BLUE
                            )
                        )
                )
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { msg ->
            if (!msg.isNullOrEmpty()) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // --- Ciclo de vida del MapView ---
    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }
}
