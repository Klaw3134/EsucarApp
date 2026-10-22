package com.example.esucarapp.ui.pantallas.estudiante

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.esucarapp.data.DatosSimulados
import com.example.esucarapp.ui.componentes.*
import com.example.esucarapp.ui.theme.*

/*
 * Visor de material paginado
 * El estudiante puede descargar y marcar como completado
 * El docente lo abre en modo lectura
 */
@Composable
fun VisorMaterialScreen(
    materialId: Int,
    usuario: String,
    soloLectura: Boolean,
    onVolver: () -> Unit
) {
    val material = DatosSimulados.buscarMaterial(materialId)
    if (material == null) {
        MensajeNoEncontrado("Material no encontrado")
        return
    }
    val asignatura = DatosSimulados.buscarAsignatura(material.asignaturaId)
    var pagina by remember { mutableStateOf(1) }
    val contenido = DatosSimulados.paginaDe(material, pagina)
    val completado = DatosSimulados.estaCompletado(usuario, material.id)
    val descargado = DatosSimulados.estaDescargado(usuario, material.id)

    Scaffold(
        topBar = { BarraSuperior("Visor de material", onVolver = onVolver) },
        bottomBar = {
            if (!soloLectura) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Blanco)
                        .navigationBarsPadding()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { DatosSimulados.marcarDescargado(usuario, material.id) },
                        enabled = !descargado,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            if (descargado) Icons.Default.CloudDone else Icons.Default.Download,
                            contentDescription = null,
                            tint = Verde
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(if (descargado) "Descargado" else "Descargar")
                    }
                    FilledTonalButton(
                        onClick = { DatosSimulados.alternarCompletado(usuario, material.id) },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = if (completado) VerdeClaro else GrisChip,
                            contentColor = if (completado) VerdeOscuro else TextoPrincipal
                        )
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null)
                        Spacer(Modifier.width(6.dp))
                        Text(if (completado) "Completado" else "Marcar completado")
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(material.titulo, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(
                "${asignatura?.nombre ?: ""} · Unidad ${material.unidad}",
                style = MaterialTheme.typography.bodyMedium,
                color = GrisTexto
            )

            // "Hoja" del documento
            TarjetaContorno(colorBorde = GrisBorde, colorFondo = Blanco) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text("ESUCAR", color = Verde, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                    Spacer(Modifier.weight(1f))
                    Text(material.tipo.uppercase(), color = GrisTexto, style = MaterialTheme.typography.labelSmall)
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = GrisBorde)
                Text(contenido.titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))
                Text(contenido.introduccion, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(12.dp))
                Text(contenido.subtitulo, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                // Lista numerada con forEachIndexed
                contenido.puntos.forEachIndexed { i, punto ->
                    Text("${i + 1}. $punto", style = MaterialTheme.typography.bodyMedium)
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    "Actividad de ejemplo. Complementa la lectura con el material y las orientaciones del docente.",
                    style = MaterialTheme.typography.bodySmall,
                    color = GrisTexto,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(FondoTarjeta)
                        .padding(12.dp)
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    "$pagina",
                    style = MaterialTheme.typography.labelSmall,
                    color = GrisTexto,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            // Paginación
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedIconButton(onClick = { pagina-- }, enabled = pagina > 1) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Página anterior")
                }
                Text(
                    "Página $pagina de ${material.totalPaginas}",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                OutlinedIconButton(onClick = { pagina++ }, enabled = pagina < material.totalPaginas) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Página siguiente", tint = Verde)
                }
            }
        }
    }
}
