package com.incidenciasapp.ui.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.incidenciasapp.R
import com.incidenciasapp.dto.incidencia.IncidenciaListadoDto

class IncidenciaAdapter(
    private var lista: List<IncidenciaListadoDto> ,
    private val onItemClick: (IncidenciaListadoDto) -> Unit // Para abrir el detalle después
) : RecyclerView.Adapter<IncidenciaAdapter.IncidenciaViewHolder>() {

    class IncidenciaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val descripcion: TextView = view.findViewById(R.id.tvDescripcion)
        val fecha: TextView = view.findViewById(R.id.tvFecha)
        val iconoFondo: View = view.findViewById(R.id.viewIconType)
        val iconoTipo: ImageView = view.findViewById(R.id.ivTipoIcon)



        // Función para bindear datos
        fun bind(item: IncidenciaListadoDto) {
            descripcion.text = item.descripcion ?: "Sin descripción"

            // Formatear fecha simple (puedes mejorar esto con una función de tiempo transcurrido)
            fecha.text = item.fechaIncidencia
                ?.replace("T", " ")
                ?.take(16)
                ?: "Sin fecha"


            val (iconRes, color) = when (item.idTipo) {
                1 -> R.drawable.ic_robo to "#FF4B4B"   // Robo
                2 -> R.drawable.ic_car_crash to "#FFB347" // Accidente
                3 -> R.drawable.ic_warning to "#FACC15"   // Peligro vía
                4 -> R.drawable.ic_medical to "#22C55E"   // Emergencia médica
                else -> R.drawable.ic_other to "#64748B"  // Otro
            }
            iconoTipo.setImageResource(iconRes)

            iconoFondo.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor(color))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncidenciaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_incidencia, parent, false)
        return IncidenciaViewHolder(view)
    }

    override fun onBindViewHolder(holder: IncidenciaViewHolder, position: Int) {
        val item = lista[position]
        holder.bind(item)

        // Listener para clics
        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    override fun getItemCount(): Int = lista.size

    // Función para actualizar la lista desde el Fragment
    fun updateData(nuevaLista: List<IncidenciaListadoDto>) {
        this.lista = nuevaLista
        notifyDataSetChanged()
    }
}