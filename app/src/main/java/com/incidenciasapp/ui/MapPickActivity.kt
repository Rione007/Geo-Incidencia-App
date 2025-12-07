package com.incidenciasapp.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.incidenciasapp.R


class MapPickActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private var marker: Marker? = null
    private var selectedLatLng: LatLng? = null

    // Coordenadas de Trujillo
    private val trujilloCenter = LatLng(-8.1116, -79.0288)

    // Límites aproximados de Trujillo
    private val trujilloBounds = LatLngBounds(
        LatLng(-8.1800, -79.0900), // suroeste
        LatLng(-8.0400, -78.9600)  // noreste
    )

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_pick)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        findViewById<FloatingActionButton>(R.id.fab_confirm).setOnClickListener {
            selectedLatLng?.let { latLng ->
                val data = Intent().apply {
                    putExtra("lat", latLng.latitude)
                    putExtra("lng", latLng.longitude)
                }
                setResult(Activity.RESULT_OK, data)
                finish()
            } ?: run {
                Toast.makeText(this, "Selecciona un punto en el mapa", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.uiSettings.isZoomControlsEnabled = true

        // Centrar el mapa en Trujillo con zoom apropiado
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(trujilloCenter, 13f))

        // Limitar el movimiento del mapa a Trujillo
        googleMap.setLatLngBoundsForCameraTarget(trujilloBounds)

        // Limitar el zoom mínimo y máximo
        googleMap.setMinZoomPreference(11f)
        googleMap.setMaxZoomPreference(18f)

        googleMap.setOnMapClickListener { latLng ->
            marker?.remove()
            marker = googleMap.addMarker(MarkerOptions().position(latLng).title("Ubicación seleccionada"))
            selectedLatLng = latLng
        }
    }
}