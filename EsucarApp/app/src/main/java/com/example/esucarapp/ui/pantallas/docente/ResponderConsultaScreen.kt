package com.example.esucarapp.ui.pantallas.docente

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.esucarapp.data.DatosSimulados
import com.example.esucarapp.ui.componentes.*
import com.example.esucarapp.ui.theme.*

/*
 * Formulario del docente para responder una consulta (mockup "Responder consulta").
 * Sin backend: la respuesta actualiza la consulta en memoria y el estudiante la ve en "Mis consultas".
 */
@Composable
fun ResponderConsultaScreen(
    consultaId: Int,
    onVolver: () -> Unit,
    onAbrirMaterial: (Int) -> Unit
) {
    val consulta = DatosSimulados.buscarConsulta(consultaId)
    if (consulta == null) {
        MensajeNoEncontrado("Consulta no encontrada")
        return
    }
    val asignatura = DatosSimulados.buscarAsignatura(consulta.asignaturaId)
    val material = DatosSimulados.buscarMaterial(consulta.materialId)
    var respuesta by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            BarraSuperior(
                if (consulta.respondida) "Consulta respondida" else "Responder consulta",
                onVolver = onVolver
            )
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
            Text(asignatura?.nombre ?: "", color = Verde, fontWeight = FontWeight.SemiBold)
            Text(consulta.asunto, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(consulta.fecha, style = MaterialTheme.typography.bodySmall, color = GrisTexto, modifier = Modifier.weight(1f))
                ChipEstado(consulta.respondida)
            }

            TarjetaContorno {
                Text("Estudiante: ${consulta.estudiante}", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))
                Text(consulta.mensaje, style = MaterialTheme.typography.bodyMedium)
            }

            if (material != null) {
                Text("Material relacionado", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                TarjetaMaterialRelacionado(material, asignatura?.nombre ?: "") { onAbrirMaterial(material.id) }
            }

            if (consulta.respondida) {
                // Ya respondida: se muestra la respuesta (solo lectura)
                TarjetaContorno {
                    Text("Tu respuesta", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(consulta.fechaRespuesta ?: "", style = MaterialTheme.typography.bodySmall, color = GrisTexto)
                    Spacer(Modifier.height(8.dp))
                    Text(consulta.respuesta ?: "")
                }
            } else {
                Text("Tu respuesta", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = respuesta,
                    onValueChange = { respuesta = it; mensajeError = "" },
                    placeholder = { Text("Escribe una respuesta para el estudiante...") },
                    minLines = 5,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    "Al enviarla, la consulta quedará respondida y el estudiante podrá leerla en «Mis consultas».",
                    style = MaterialTheme.typography.bodySmall,
                    color = GrisTexto
                )
                if (mensajeError.isNotBlank()) {
                    Text(mensajeError, color = MaterialTheme.colorScheme.error)
                }
                Button(
                    onClick = {
                        if (respuesta.trim().length < 10) {
                            mensajeError = "La respuesta debe tener al menos 10 caracteres"
                        } else {
                            DatosSimulados.responderConsulta(consulta.id, respuesta.trim())
                            Toast.makeText(context, "Respuesta enviada a ${consulta.estudiante}", Toast.LENGTH_SHORT).show()
                            onVolver()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Enviar respuesta")
                }
            }
        }
    }
}
