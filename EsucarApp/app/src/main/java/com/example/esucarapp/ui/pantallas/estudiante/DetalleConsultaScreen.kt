package com.example.esucarapp.ui.pantallas.estudiante

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.esucarapp.data.DatosSimulados
import com.example.esucarapp.ui.componentes.*
import com.example.esucarapp.ui.theme.*

// Detalle de una consulta del estudiante con la respuesta del docente
@Composable
fun DetalleConsultaScreen(
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

    Scaffold(topBar = { BarraSuperior("Detalle de consulta", onVolver = onVolver) }) { innerPadding ->
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

            Text("Tu pregunta", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            TarjetaContorno(colorBorde = GrisBorde) {
                Text(consulta.mensaje, style = MaterialTheme.typography.bodyMedium)
            }

            if (material != null) {
                Text("Material relacionado", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                TarjetaMaterialRelacionado(material, asignatura?.nombre ?: "") { onAbrirMaterial(material.id) }
            }

            if (consulta.respondida) {
                TarjetaContorno {
                    Text("Respuesta del docente", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(consulta.fechaRespuesta ?: "", style = MaterialTheme.typography.bodySmall, color = GrisTexto)
                    Spacer(Modifier.height(8.dp))
                    Text(consulta.respuesta ?: "", style = MaterialTheme.typography.bodyMedium)
                }
            } else {
                TarjetaContorno(colorBorde = GrisBorde, colorFondo = Blanco) {
                    Text("Esperando respuesta del docente", fontWeight = FontWeight.SemiBold)
                    Text(
                        "Te avisaremos aquí cuando ${asignatura?.docente ?: "el docente"} responda.",
                        style = MaterialTheme.typography.bodySmall,
                        color = GrisTexto
                    )
                }
            }
        }
    }
}
