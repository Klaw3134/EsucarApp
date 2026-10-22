package com.example.esucarapp.data

// Modelos de datos

data class Unidad(
    val numero: Int,
    val titulo: String
)

data class Asignatura(
    val id: Int,
    val codigo: String,
    val nombre: String,
    val docente: String,
    val unidades: List<Unidad>
)

// tipo: "Guía", "Lectura", "Presentación" o "Video"
data class Material(
    val id: Int,
    val asignaturaId: Int,
    val unidad: Int,
    val titulo: String,
    val tipo: String,
    val descripcion: String = "",
    val totalPaginas: Int = 8
)

data class Consulta(
    val id: Int,
    val estudiante: String,     // emisor
    val asignaturaId: Int,      // receptor = docente de la asignatura
    val materialId: Int,        // recurso al que se refiere la consulta
    val asunto: String,
    val mensaje: String,
    val fecha: String,
    val respuesta: String? = null,
    val fechaRespuesta: String? = null
) {
    val respondida: Boolean
        get() = respuesta != null
}

data class PaginaMaterial(
    val titulo: String,
    val introduccion: String,
    val subtitulo: String,
    val puntos: List<String>
)
