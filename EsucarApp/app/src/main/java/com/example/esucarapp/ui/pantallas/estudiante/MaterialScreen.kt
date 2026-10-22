package com.example.esucarapp.ui.pantallas.estudiante

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.esucarapp.data.DatosSimulados
import com.example.esucarapp.ui.componentes.*
import com.example.esucarapp.ui.theme.*
import kotlinx.coroutines.launch

/*
 * Detalle de un material
 * Ver / Descargar / Completado + formulario "Consultar al Docente".
 * El formulario NO tiene backend la consulta se agrega a la lista en memoria.
 */
@Composable
fun MaterialScreen(
    materialId: Int,
    usuario: String,
    onVolver: () -> Unit,
    onVer: (Int) -> Unit
) {
    val material = DatosSimulados.buscarMaterial(materialId)
    if (material == null) {
        MensajeNoEncontrado("Material no encontrado")
        return
    }
    val asignatura = DatosSimulados.buscarAsignatura(material.asignaturaId)
    val completado = DatosSimulados.estaCompletado(usuario, material.id)
    val descargado = DatosSimulados.estaDescargado(usuario, material.id)

    var asunto by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { BarraSuperior(if (material.tipo == "Guía") "Guía de Estudio" else material.tipo, onVolver = onVolver) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
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
                "${asignatura?.nombre ?: ""} · Unidad ${material.unidad} · ${material.tipo}",
                style = MaterialTheme.typography.bodyMedium,
                color = GrisTexto
            )
            if (material.descripcion.isNotBlank()) {
                Text(material.descripcion, style = MaterialTheme.typography.bodyMedium)
            }

            // Botones de acción
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = { onVer(material.id) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Ver")
                }
                OutlinedButton(
                    onClick = {
                        DatosSimulados.marcarDescargado(usuario, material.id)
                        scope.launch { snackbarHostState.showSnackbar("Material disponible sin conexión") }
                    },
                    enabled = !descargado,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    Icon(
                        if (descargado) Icons.Default.CloudDone else Icons.Default.Download,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(if (descargado) "Descargado" else "Descargar")
                }
                FilledTonalButton(
                    onClick = { DatosSimulados.alternarCompletado(usuario, material.id) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = if (completado) VerdeClaro else GrisChip,
                        contentColor = if (completado) VerdeOscuro else TextoPrincipal
                    )
                ) {
                    if (completado) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                    }
                    Text(if (completado) "Completado" else "Completar")
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = GrisBorde)

            // Formulario "Consultar al Docente"
            Text("Consultar al Docente", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(
                "Tu consulta llegará a ${asignatura?.docente ?: "el docente"} vinculada a este material.",
                style = MaterialTheme.typography.bodySmall,
                color = GrisTexto
            )
            OutlinedTextField(
                value = asunto,
                onValueChange = { asunto = it; mensajeError = "" },
                label = { Text("Asunto") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = mensaje,
                onValueChange = { mensaje = it; mensajeError = "" },
                label = { Text("Mensaje") },
                minLines = 4,
                modifier = Modifier.fillMaxWidth()
            )
            if (mensajeError.isNotBlank()) {
                Text(mensajeError, color = MaterialTheme.colorScheme.error)
            }
            Button(
                onClick = {
                    when {
                        asunto.isBlank() || mensaje.isBlank() -> mensajeError = "Completa el asunto y el mensaje"
                        mensaje.trim().length < 10 -> mensajeError = "El mensaje debe tener al menos 10 caracteres"
                        else -> {
                            DatosSimulados.enviarConsulta(usuario, material, asunto.trim(), mensaje.trim())
                            asunto = ""
                            mensaje = ""
                            scope.launch { snackbarHostState.showSnackbar("Consulta enviada. Revísala en «Consultas».") }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Enviar Consulta")
            }
        }
    }
}
