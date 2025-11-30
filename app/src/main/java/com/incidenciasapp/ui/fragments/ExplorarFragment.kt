package com.incidenciasapp.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.TileOverlayOptions
import com.google.maps.android.heatmaps.HeatmapTileProvider
import com.incidenciasapp.R

class ExplorarFragment : Fragment() {

    private var mapView: MapView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_explorar, container, false)
        mapView = root.findViewById(R.id.mapView)
        mapView?.onCreate(savedInstanceState)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView?.getMapAsync { googleMap ->
            MapsInitializer.initialize(requireContext())

            // Centro inicial
            val trujillo = LatLng(-8.1091, -79.0215)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(trujillo, 12f))

            // Ejemplo de puntos (reemplazar con los datos de la incidencia)
            val points = listOf(
                LatLng(-8.11, -79.02),
                LatLng(-8.12, -79.03),
                LatLng(-8.10, -79.01),
                // ... más puntos
            )


            // Prueba de marcadores  para las incidencias
            points.forEach { latLng ->
                googleMap.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title("Incidencia")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                )
            }

            //Con punto de calor de prueba xd
//            // Crear HeatmapTileProvider y añadir overlay
//            val provider = HeatmapTileProvider.Builder()
//                .data(points)
//                .radius(30) // ajusta según densidad y zoom
//                .build()
//
//            googleMap.addTileOverlay(TileOverlayOptions().tileProvider(provider))
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }
}