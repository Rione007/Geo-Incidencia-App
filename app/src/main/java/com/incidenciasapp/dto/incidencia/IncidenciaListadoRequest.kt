package com.incidenciasapp.dto.incidencia

data class IncidenciaListadoRequest(
    val tipo: Int? = null,
    val subtipo: Int? = null,
    val fechaDesde: String? = null,
    val fechaHasta: String? = null,
    val limit: Int = 50
)
