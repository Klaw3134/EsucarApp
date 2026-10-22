package com.example.esucarapp.navegacion

object Rutas {
    // Comunes
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val VISOR = "visor/{materialId}"

    // Estudiante
    const val EST_INICIO = "estudiante/inicio"
    const val EST_ASIGNATURA = "estudiante/asignatura/{asignaturaId}"
    const val EST_MATERIAL = "estudiante/material/{materialId}"
    const val EST_PROGRESO = "estudiante/progreso"
    const val EST_CONSULTAS = "estudiante/consultas"
    const val EST_DETALLE_CONSULTA = "estudiante/consulta/{consultaId}"

    // Docente
    const val DOC_INICIO = "docente/inicio"
    const val DOC_ASIGNATURA = "docente/asignatura/{asignaturaId}"
    const val DOC_PUBLICAR = "docente/publicar/{asignaturaId}"
    const val DOC_BANDEJA = "docente/bandeja"
    const val DOC_RESPONDER = "docente/responder/{consultaId}"

    // Funciones para armar la ruta con el id real
    fun visor(materialId: Int) = "visor/$materialId"
    fun estAsignatura(id: Int) = "estudiante/asignatura/$id"
    fun estMaterial(id: Int) = "estudiante/material/$id"
    fun estDetalleConsulta(id: Int) = "estudiante/consulta/$id"
    fun docAsignatura(id: Int) = "docente/asignatura/$id"
    fun docPublicar(id: Int) = "docente/publicar/$id"
    fun docResponder(id: Int) = "docente/responder/$id"
}
