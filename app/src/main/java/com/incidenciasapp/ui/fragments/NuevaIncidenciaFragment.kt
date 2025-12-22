package com.incidenciasapp.ui.fragments

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.incidenciasapp.BuildConfig
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import com.incidenciasapp.R
import com.incidenciasapp.data.local.UserPreferences
import com.incidenciasapp.data.remote.api.IncidenciaApiService
import com.incidenciasapp.data.remote.api.SubtipoApiService
import com.incidenciasapp.data.remote.api.TipoApiService
import com.incidenciasapp.data.remote.azure.AzureStorageService
import com.incidenciasapp.data.remote.retrofit.RetrofitClient
import com.incidenciasapp.data.repository.IncidenciaRepository
import com.incidenciasapp.data.repository.SubtipoRepository
import com.incidenciasapp.data.repository.TipoRepository
import com.incidenciasapp.dto.Subtipo.SubtipoItem
import com.incidenciasapp.dto.Tipo.TipoItem
import com.incidenciasapp.dto.incidencia.IncidenciaRequest
import com.incidenciasapp.ui.MapPickActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.collections.emptyList
class NuevaIncidenciaFragment : Fragment() {

    private var lat: Double? = null
    private var lng: Double? = null
//    private val selectedImageUris = mutableListOf<Uri>()
//    private val maxImages = 3
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private var chosenDate = Calendar.getInstance()
//    private lateinit var containerImages: ViewGroup

    private var tiposList: List<TipoItem> = emptyList()
    private var subtiposList: List<SubtipoItem> = emptyList()

//    private val pickImagesLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == android.app.Activity.RESULT_OK) {
//            val data = result.data ?: return@registerForActivityResult
//            selectedImageUris.clear()
//            data.clipData?.let { clip ->
//                val count = clip.itemCount.coerceAtMost(maxImages)
//                for (i in 0 until count) selectedImageUris.add(clip.getItemAt(i).uri)
//            } ?: data.data?.let { uri ->
//                selectedImageUris.add(uri)
//            }
//            if (selectedImageUris.size > maxImages) selectedImageUris.subList(maxImages, selectedImageUris.size).clear()
//            showSelectedImages()
//        }
//    }

    private val pickLocationLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val data = result.data ?: return@registerForActivityResult
            lat = data.getDoubleExtra("lat", Double.NaN).takeIf { !it.isNaN() }
            lng = data.getDoubleExtra("lng", Double.NaN).takeIf { !it.isNaN() }
            val tvCoords = view?.findViewById<TextView>(R.id.tv_coords)
            tvCoords?.text = "Coordenadas: ${lat?.toString() ?: "---"}, ${lng?.toString() ?: "---"}"
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_nueva_incidencia, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val spinnerTipo = view.findViewById<Spinner>(R.id.spinner_tipo)
        val spinnerSubtipo = view.findViewById<Spinner>(R.id.spinner_subtipo)
        val btnPickDate = view.findViewById<View>(R.id.btn_pick_date)
        val btnPickLocation = view.findViewById<View>(R.id.btn_pick_location)
        //val btnPickImages = view.findViewById<View>(R.id.btn_pick_images)
        val btnSubmit = view.findViewById<View>(R.id.btn_submit)
        val tvDate = view.findViewById<TextView>(R.id.tv_date)
        val etDireccion = view.findViewById<EditText>(R.id.et_direccion)
        val etDescripcion = view.findViewById<EditText>(R.id.et_descripcion)
        //containerImages = view.findViewById(R.id.container_images)

        spinnerTipo.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listOf("Cargando..."))
        spinnerSubtipo.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listOf("Cargando..."))

        val tipoService = RetrofitClient.create(TipoApiService::class.java)
        val subtipoService = RetrofitClient.create(SubtipoApiService::class.java)
        val tipoRepo = TipoRepository(tipoService)
        val subtipoRepo = SubtipoRepository(subtipoService)

        fun updateSubtiposFor(tipoId: Int) {
            val filtered = if (tipoId == 0) emptyList() else subtiposList.filter { it.idTipo == tipoId }
            val nombres = listOf("Seleccionar") + filtered.map { it.nombre }
            val adapterSub = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, nombres)
            adapterSub.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerSubtipo.adapter = adapterSub
            spinnerSubtipo.tag = filtered
        }

        lifecycleScope.launch {
            val tiposResult = tipoRepo.obtenerTipos()
            if (tiposResult.isSuccess) {
                tiposList = tiposResult.getOrNull() ?: emptyList()
                val nombres = listOf("Seleccionar") + tiposList.map { it.nombre }
                val adapterTipo = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, nombres)
                adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerTipo.adapter = adapterTipo
            } else {
                Toast.makeText(requireContext(), "Error al cargar tipos", Toast.LENGTH_SHORT).show()
            }

            val subtiposResult = subtipoRepo.obtenerSubtipos()
            if (subtiposResult.isSuccess) {
                subtiposList = subtiposResult.getOrNull() ?: emptyList()
                updateSubtiposFor(0)
            } else {
                Toast.makeText(requireContext(), "Error al cargar subtipos", Toast.LENGTH_SHORT).show()
            }
        }

        spinnerTipo.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>, viewItem: View?, position: Int, id: Long) {
                val tipoId = if (position == 0) 0 else tiposList.getOrNull(position - 1)?.idTipo ?: 0
                updateSubtiposFor(tipoId)
            }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>) {}
        }

        btnPickDate.setOnClickListener {
            val c = chosenDate
            DatePickerDialog(requireContext(), { _, y, m, d ->
                c.set(y, m, d)
                tvDate.text = "Fecha: ${dateFormat.format(c.time)}"
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }

        btnPickLocation.setOnClickListener {
            pickLocationLauncher.launch(Intent(requireContext(), MapPickActivity::class.java))
        }

//        btnPickImages.setOnClickListener {
//            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
//                type = "image/*"
//                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//            }
//            pickImagesLauncher.launch(Intent.createChooser(intent, "Seleccionar imágenes"))
//        }

        btnSubmit.setOnClickListener {
            val tipoPos = spinnerTipo.selectedItemPosition
            val subtipoPos = spinnerSubtipo.selectedItemPosition

            if (tipoPos <= 0 || subtipoPos <= 0) {
                Toast.makeText(requireContext(), "Selecciona tipo y subtipo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedTipo = tiposList.getOrNull(tipoPos - 1)
            val filteredSubtipos = spinnerSubtipo.tag as? List<SubtipoItem> ?: emptyList()
            val selectedSubtipo = filteredSubtipos.getOrNull(subtipoPos - 1)

            if (selectedTipo == null || selectedSubtipo == null) {
                Toast.makeText(requireContext(), "Error en selección", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    btnSubmit.isEnabled = false

                    val userPrefs = UserPreferences(requireContext())
                    val idUsuario = userPrefs.getUserId().first()

                    if (idUsuario == null) {
                        Toast.makeText(requireContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show()
                        btnSubmit.isEnabled = true
                        return@launch
                    }

//                    Toast.makeText(requireContext(), "Subiendo imágenes...", Toast.LENGTH_SHORT).show()

//                    val azureService = AzureStorageService(
//                        accountName = BuildConfig.AZURE_ACCOUNT_NAME,
//                        containerName = BuildConfig.AZURE_CONTAINER_NAME,
//                        sasToken = BuildConfig.AZURE_SAS_TOKEN
//                    )
//
//                    val uploadResult = azureService.uploadMultipleImages(requireContext(), selectedImageUris)

//                    if (uploadResult.isFailure) {
//                        Toast.makeText(requireContext(), "Error: ${uploadResult.exceptionOrNull()?.message}", Toast.LENGTH_SHORT).show()
//                        btnSubmit.isEnabled = true
//                        return@launch
//                    }

//                    val imageUrls = uploadResult.getOrNull() ?: emptyList()

                    val request = IncidenciaRequest(
                        idUsuario = idUsuario,
                        latitud = lat ?: 0.0,
                        longitud = lng ?: 0.0,
                        idTipo = selectedTipo.idTipo,
                        idSubtipo = selectedSubtipo.idSubtipo,
                        descripcion = etDescripcion.text.toString().ifBlank { null },
                        direccionReferencia = etDireccion.text.toString().ifBlank { null },
                        fotoUrl1 = null,
                        fotoUrl2 = null,
                        fotoUrl3 = null,
                        fechaIncidencia = dateFormat.format(chosenDate.time)
                    )


                    val incidenciaService = RetrofitClient.create(IncidenciaApiService::class.java)
                    val incidenciaRepo = IncidenciaRepository(incidenciaService)

                    val result = incidenciaRepo.createIncidencia(request)
                    if (result.isSuccess) {
                        val resp = result.getOrNull()
                        Toast.makeText(requireContext(), "Incidencia creada: ${resp?.mensaje ?: "OK"}", Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(requireContext(), "Error al crear incidencia: ${result.exceptionOrNull()?.message}", Toast.LENGTH_LONG).show()
                    }

                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
                } finally {
                    btnSubmit.isEnabled = true
                }
            }
        }
    }

//    private fun showSelectedImages() {
//        containerImages.removeAllViews()
//        selectedImageUris.forEach { uri ->
//            val iv = ImageView(requireContext()).apply {
//                layoutParams = ViewGroup.LayoutParams(200, 200)
//                scaleType = ImageView.ScaleType.CENTER_CROP
//                setImageURI(uri)
//            }
//            containerImages.addView(iv)
//        }
//    }
}