package com.example.esucarapp.ui.pantallas.estudiante

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.esucarapp.data.DatosSimulados
import com.example.esucarapp.data.Material
import com.example.esucarapp.ui.componentes.*
import com.example.esucarapp.ui.theme.*

/*
 * Detalle de asignatura: unidades temáticas desplegables con sus materiales.
 * La usan ambos roles:
 *  - Estudiante: ve qué materiales tiene completados.
 *  - Docente ve un botón para publicar material.
 */
@Composable
fun AsignaturaScreen(
    asignaturaId: Int,
    usuario: String,
    esDocente: Boolean,
    onVolver: () -> Unit,
    onAbrirMaterial: (Int) -> Unit,
    onPublicar: () -> Unit = {}
) {
    val asignatura = DatosSimulados.buscarAsignatura(asignaturaId)
    if (asignatura == null) {
        MensajeNoEncontrado("Asignatura no encontrada")
        return
    }
    var unidadAbierta by remember { mutableStateOf(1) }

    Scaffold(
        topBar = { BarraSuperior(asignatura.nombre, onVolver = onVolver) },
        floatingActionButton = {
            if (esDocente) {
                ExtendedFloatingActionButton(
                    onClick = onPublicar,
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    text = { Text("Publicar material") },
                    containerColor = Verde,
                    contentColor = Blanco
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    "${asignatura.codigo} · ${asignatura.docente}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GrisTexto
                )
                if (!esDocente) {
                    Spacer(Modifier.height(10.dp))
                    FilaProgreso("Tu avance", DatosSimulados.progresoAsignatura(usuario, asignatura.id))
                }
            }
            items(asignatura.unidades, key = { it.numero }) { unidad ->
                val materialesUnidad = DatosSimulados.materialesDeUnidad(asignatura.id, unidad.numero)
                val abierta = unidadAbierta == unidad.numero
                val revisados = materialesUnidad.count { DatosSimulados.estaCompletado(usuario, it.id) }
                val forma = RoundedCornerShape(10.dp)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(forma)
                        .background(FondoTarjeta)
                        .border(if (abierta) 1.5.dp else 1.dp, if (abierta) Verde else GrisBorde, forma)
                ) {
                    // Cabecera de la unidad
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { unidadAbierta = if (abierta) 0 else unidad.numero }
                            .padding(horizontal = 14.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Unidad ${unidad.numero}: ${unidad.titulo}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                if (esDocente) "${materialesUnidad.size} materiales publicados"
                                else "$revisados de ${materialesUnidad.size} revisados",
                                style = MaterialTheme.typography.bodySmall,
                                color = GrisTexto
                            )
                        }
                        Icon(
                            if (abierta) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (abierta) "Cerrar unidad" else "Abrir unidad"
                        )
                    }
                    // Materiales de la unidad
                    if (abierta) {
                        materialesUnidad.forEach { material ->
                            HorizontalDivider(color = GrisBorde)
                            FilaMaterial(
                                material = material,
                                completado = !esDocente && DatosSimulados.estaCompletado(usuario, material.id),
                                onClick = { onAbrirMaterial(material.id) }
                            )
                        }
                        if (materialesUnidad.isEmpty()) {
                            HorizontalDivider(color = GrisBorde)
                            Text(
                                "Aún no hay materiales en esta unidad",
                                modifier = Modifier.padding(14.dp),
                                color = GrisTexto
                            )
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(72.dp)) } // espacio para el botón flotante
        }
    }
}

@Composable
private fun FilaMaterial(material: Material, completado: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Blanco)
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(iconoDeTipo(material.tipo), contentDescription = material.tipo, tint = Verde)
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(material.titulo, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
            Text(material.tipo, style = MaterialTheme.typography.bodySmall, color = GrisTexto)
        }
        if (completado) {
            Icon(Icons.Default.CheckCircle, contentDescription = "Completado", tint = Verde, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(6.dp))
        }
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = GrisTexto)
    }
}
