package com.example.esucarapp.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


object DatosSimulados {

    val asignaturas: List<Asignatura> = listOf(
        Asignatura(
            1, "DPE-101", "Derecho Penal", "Prof. Rojas",
            listOf(
                Unidad(1, "Introducción al Derecho"),
                Unidad(2, "Teoría del Delito"),
                Unidad(3, "Penas y Medidas")
            )
        ),
        Asignatura(
            2, "PPO-102", "Procedimientos Policiales", "Prof. Muñoz",
            listOf(
                Unidad(1, "Marco Normativo"),
                Unidad(2, "Control de Identidad"),
                Unidad(3, "Procedimientos en Flagrancia")
            )
        ),
        Asignatura(
            3, "DFP-103", "Defensa Personal", "Prof. Soto",
            listOf(
                Unidad(1, "Fundamentos"),
                Unidad(2, "Técnicas de Control"),
                Unidad(3, "Uso Proporcional de la Fuerza")
            )
        ),
        Asignatura(
            4, "ETP-104", "Ética Profesional", "Prof. Herrera",
            listOf(
                Unidad(1, "Fundamentos de la Ética"),
                Unidad(2, "Probidad y Transparencia"),
                Unidad(3, "Derechos Humanos y Función Policial")
            )
        )
    )


    val tiposMaterial: List<String> = listOf("Guía", "Lectura", "Presentación", "Video")


    val materiales = mutableStateListOf<Material>()
    val consultas = mutableStateListOf<Consulta>()

    private val completadosPorUsuario = mutableStateMapOf<String, Set<Int>>()
    private val descargadosPorUsuario = mutableStateMapOf<String, Set<Int>>()

    init {
        cargarMateriales()
        cargarConsultas()

        completadosPorUsuario["pepito"] = setOf(
            1, 2, 3, 4, 5, 6,          // Derecho Penal 6/10
            11, 12, 13, 14,            // Procedimientos Policiales 4/10
            21, 22, 23, 24, 25, 26, 27, 28, // Defensa Personal 8/10
            31, 32                     // Ética Profesional 2/10
        )
        descargadosPorUsuario["pepito"] = setOf(1, 25)
    }


    private fun agregarMaterial(asignaturaId: Int, unidad: Int, titulo: String, tipo: String, descripcion: String = "") {
        val nuevoId = (materiales.maxOfOrNull { it.id } ?: 0) + 1
        materiales.add(Material(nuevoId, asignaturaId, unidad, titulo, tipo, descripcion))
    }

    private fun cargarMateriales() {
        // Derecho Penal (ids 1-10)
        agregarMaterial(1, 1, "Principios del Derecho Penal", "Guía")
        agregarMaterial(1, 1, "Código Penal: Libro Primero", "Lectura")
        agregarMaterial(1, 1, "Clase 1: Fuentes del Derecho", "Presentación")
        agregarMaterial(1, 1, "Análisis de Caso Práctico", "Guía")
        agregarMaterial(1, 2, "Elementos del delito", "Lectura")
        agregarMaterial(1, 2, "Tipicidad y antijuridicidad", "Presentación")
        agregarMaterial(1, 2, "Video: Teoría del delito", "Video")
        agregarMaterial(1, 3, "Clasificación de las penas", "Lectura")
        agregarMaterial(1, 3, "Medidas alternativas", "Presentación")
        agregarMaterial(1, 3, "Pauta de ejercicios Unidad 3", "Guía")
        // Procedimientos Policiales (ids 11-20)
        agregarMaterial(2, 1, "Ley Orgánica de Carabineros", "Lectura")
        agregarMaterial(2, 1, "Clase 1: Marco normativo", "Presentación")
        agregarMaterial(2, 1, "Guía de conceptos básicos", "Guía")
        agregarMaterial(2, 1, "Video: Rol de Carabineros", "Video")
        agregarMaterial(2, 2, "Pasos para el control de identidad", "Guía")
        agregarMaterial(2, 2, "Art. 85 Código Procesal Penal", "Lectura")
        agregarMaterial(2, 2, "Casos de control preventivo", "Presentación")
        agregarMaterial(2, 3, "Detención en flagrancia", "Lectura")
        agregarMaterial(2, 3, "Lectura de derechos", "Guía")
        agregarMaterial(2, 3, "Video: Procedimiento simulado", "Video")
        // Defensa Personal (ids 21-30)
        agregarMaterial(3, 1, "Postura y desplazamientos", "Guía")
        agregarMaterial(3, 1, "Calentamiento y prevención de lesiones", "Lectura")
        agregarMaterial(3, 1, "Video: Postura base", "Video")
        agregarMaterial(3, 1, "Clase 1: Fundamentos", "Presentación")
        agregarMaterial(3, 2, "Técnicas de inmovilización", "Guía")
        agregarMaterial(3, 2, "Video: Técnicas de control", "Video")
        agregarMaterial(3, 2, "Pauta de práctica supervisada", "Guía")
        agregarMaterial(3, 3, "Protocolo de uso de la fuerza", "Lectura")
        agregarMaterial(3, 3, "Niveles de resistencia", "Presentación")
        agregarMaterial(3, 3, "Análisis de casos", "Guía")
        // Ética Profesional (ids 31-40)
        agregarMaterial(4, 1, "Introducción a la ética", "Lectura")
        agregarMaterial(4, 1, "Clase 1: Valores institucionales", "Presentación")
        agregarMaterial(4, 1, "Guía de reflexión", "Guía")
        agregarMaterial(4, 1, "Video: Ética en el servicio", "Video")
        agregarMaterial(4, 2, "Aplicación del deber de probidad", "Guía")
        agregarMaterial(4, 2, "Ley de probidad", "Lectura")
        agregarMaterial(4, 2, "Casos de conflicto de interés", "Presentación")
        agregarMaterial(4, 3, "DD.HH. en la función policial", "Lectura")
        agregarMaterial(4, 3, "Video: Trato digno", "Video")
        agregarMaterial(4, 3, "Pauta de evaluación formativa", "Guía")
    }

    fun buscarMaterial(id: Int): Material? {
        for (material in materiales) {
            if (material.id == id) return material
        }
        return null
    }

    fun buscarAsignatura(id: Int): Asignatura? = asignaturas.find { it.id == id }

    fun materialesDe(asignaturaId: Int): List<Material> =
        materiales.filter { it.asignaturaId == asignaturaId }

    fun materialesDeUnidad(asignaturaId: Int, unidad: Int): List<Material> =
        materialesDe(asignaturaId).filter { it.unidad == unidad }

    fun publicarMaterial(asignaturaId: Int, unidad: Int, titulo: String, tipo: String, descripcion: String) {
        agregarMaterial(asignaturaId, unidad, titulo, tipo, descripcion)
    }


    fun completados(usuario: String): Set<Int> = completadosPorUsuario[usuario] ?: emptySet()

    fun estaCompletado(usuario: String, materialId: Int): Boolean = materialId in completados(usuario)

    fun alternarCompletado(usuario: String, materialId: Int) {
        val actual = completados(usuario)
        completadosPorUsuario[usuario] =
            if (materialId in actual) actual - materialId else actual + materialId
    }

    fun estaDescargado(usuario: String, materialId: Int): Boolean =
        materialId in (descargadosPorUsuario[usuario] ?: emptySet())

    fun marcarDescargado(usuario: String, materialId: Int) {
        val actual = descargadosPorUsuario[usuario] ?: emptySet()
        descargadosPorUsuario[usuario] = actual + materialId
    }

    fun progresoAsignatura(usuario: String, asignaturaId: Int): Float {
        val lista = materialesDe(asignaturaId)
        if (lista.isEmpty()) return 0f
        val revisados = lista.count { it.id in completados(usuario) }
        return revisados.toFloat() / lista.size
    }

    fun totalRevisados(usuario: String): Int {
        var revisados = 0
        val hechos = completados(usuario)
        for (material in materiales) {
            if (material.id in hechos) revisados++
        }
        return revisados
    }

    fun totalPendientes(usuario: String): Int = materiales.size - totalRevisados(usuario)

    fun progresoGeneral(usuario: String): Float =
        if (materiales.isEmpty()) 0f else totalRevisados(usuario).toFloat() / materiales.size

    fun proximosPendientes(usuario: String, cantidad: Int = 3): List<Material> =
        materiales.filter { it.id !in completados(usuario) }.take(cantidad)

    // ================== CONSULTAS ==================

    private fun cargarConsultas() {
        consultas.addAll(
            listOf(
                Consulta(
                    1, "pepito", 4, 35, "Aplicación del deber de probidad",
                    "Profesor, ¿el deber de probidad aplica también fuera del horario de servicio? Quiero entender bien el ejemplo visto en clase.",
                    "30 sep 2026 · 09:45",
                    respuesta = "Hola, pepito. Sí, el deber de probidad es permanente y se relaciona con la conducta funcionaria en todo momento. Revisa la sección 3 de la guía, ahí está el ejemplo completo.",
                    fechaRespuesta = "01 oct 2026 · 10:15"
                ),
                Consulta(
                    2, "pepito", 3, 25, "Técnicas de inmovilización",
                    "Profesor, ¿qué aspectos debo observar para realizar correctamente las técnicas de inmovilización durante la práctica? Me cuesta coordinar la postura y el equilibrio.",
                    "02 oct 2026 · 11:20",
                    respuesta = "Hola, pepito. En la próxima práctica revisaremos tu postura y equilibrio paso a paso. Observa la demostración del docente y realiza el ejercicio de forma gradual, siempre bajo supervisión. No practiques estas técnicas sin acompañamiento.",
                    fechaRespuesta = "03 oct 2026 · 10:00"
                ),
                Consulta(
                    3, "pepito", 2, 15, "Pasos para el control de identidad",
                    "Profesor, ¿cuál es el orden correcto de los pasos del control de identidad? En la guía me confundo entre el paso 2 y el 3.",
                    "04 oct 2026 · 16:15"
                ),
                Consulta(
                    4, "pepito", 1, 1, "Duda sobre el principio de legalidad",
                    "Profesor, al leer la guía me surgió una duda sobre el principio de legalidad. ¿Cómo puedo relacionarlo con los ejemplos revisados en clase? ¿Podría orientarme sobre qué parte repasar?",
                    "05 oct 2026 · 08:30"
                )
            )
        )
    }

    fun buscarConsulta(id: Int): Consulta? = consultas.find { it.id == id }

    fun consultasDe(usuario: String): List<Consulta> =
        consultas.filter { it.estudiante == usuario }.sortedByDescending { it.id }

    fun consultasDocente(filtro: Int): List<Consulta> {
        val ordenadas = consultas.sortedByDescending { it.id }
        return when (filtro) {
            1 -> ordenadas.filter { !it.respondida }
            2 -> ordenadas.filter { it.respondida }
            else -> ordenadas
        }
    }

    fun pendientesDeAsignatura(asignaturaId: Int): Int =
        consultas.count { it.asignaturaId == asignaturaId && !it.respondida }

    fun enviarConsulta(usuario: String, material: Material, asunto: String, mensaje: String) {
        val nuevoId = (consultas.maxOfOrNull { it.id } ?: 0) + 1
        consultas.add(
            Consulta(
                id = nuevoId,
                estudiante = usuario,
                asignaturaId = material.asignaturaId,
                materialId = material.id,
                asunto = asunto,
                mensaje = mensaje,
                fecha = ahora()
            )
        )
    }

    // Formulario "Responder consulta" (sin backend)
    fun responderConsulta(consultaId: Int, texto: String) {
        val indice = consultas.indexOfFirst { it.id == consultaId }
        if (indice >= 0) {
            consultas[indice] = consultas[indice].copy(respuesta = texto, fechaRespuesta = ahora())
        }
    }

    private fun ahora(): String =
        SimpleDateFormat("dd MMM yyyy · HH:mm", Locale("es", "CL")).format(Date())

    // ================== CONTENIDO DEL VISOR ==================

    // Map: número de página -> título de la sección
    private val seccionesPorPagina: Map<Int, String> = mapOf(
        1 to "Orientaciones para el estudio",
        2 to "Objetivo de aprendizaje",
        3 to "Conceptos clave",
        4 to "Desarrollo del contenido",
        5 to "Ejemplos trabajados en clase",
        6 to "Actividad práctica",
        7 to "Preguntas de repaso",
        8 to "Síntesis y recursos"
    )

    fun paginaDe(material: Material, numero: Int): PaginaMaterial {
        val seccion = seccionesPorPagina[numero] ?: "Contenido"
        val tipo = material.tipo.lowercase()
        return when (numero) {
            1 -> PaginaMaterial(
                "$numero. $seccion",
                "Esta $tipo propone una lectura inicial de «${material.titulo}» y actividades para organizar los conceptos revisados en clase.",
                "Antes de comenzar",
                listOf(
                    "Revisa tus apuntes de la unidad.",
                    "Subraya los conceptos que necesitas aclarar.",
                    "Prepara una pregunta para discutir en clase."
                )
            )
            2 -> PaginaMaterial(
                "$numero. $seccion",
                "Identificar las ideas centrales de «${material.titulo}» y explicar su relación con los ejemplos trabajados junto al docente.",
                "Al terminar podrás",
                listOf(
                    "Definir los conceptos principales con tus palabras.",
                    "Relacionar el contenido con situaciones del servicio.",
                    "Reconocer dudas para consultar al docente."
                )
            )
            else -> PaginaMaterial(
                "$numero. $seccion",
                "En esta sección se profundiza en «${material.titulo}». Lee con atención y anota las ideas que te parezcan más importantes para la evaluación.",
                "Para trabajar",
                listOf(
                    "Lee la sección completa una vez.",
                    "Escribe un resumen de tres líneas.",
                    "Marca el material como completado al terminar."
                )
            )
        }
    }
}
