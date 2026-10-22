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

// Tablero visual de avance de autoaprendizaje (mockup "Mi Progreso General")
@Composable
fun ProgresoScreen(usuario: String, onAbrirMaterial: (Int) -> Unit) {
    val general = DatosSimulados.progresoGeneral(usuario)
    val revisados = DatosSimulados.totalRevisados(usuario)
    val pendientes = DatosSimulados.totalPendientes(usuario)
    val proximos = DatosSimulados.proximosPendientes(usuario)

    Scaffold(topBar = { BarraSuperior("Mi Progreso General") }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Círculo de avance total
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { general },
                    modifier = Modifier.size(170.dp),
                    color = Verde,
                    strokeWidth = 12.dp,
                    trackColor = VerdeClaro
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "${(general * 100).toInt()}%",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text("Total completado", style = MaterialTheme.typography.bodySmall, color = GrisTexto)
                }
            }

            // Avance por asignatura
            Text("Por asignatura", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            // map: convierte cada asignatura en un par (nombre, avance)
            val avances = DatosSimulados.asignaturas.map { it.nombre to DatosSimulados.progresoAsignatura(usuario, it.id) }
            avances.forEach { (nombre, avance) ->
                FilaProgreso(nombre, avance)
            }

            // Revisados / Pendientes
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TarjetaContorno(modifier = Modifier.weight(1f), colorFondo = VerdeClaro, colorBorde = VerdeClaro) {
                    Text("REVISADOS", style = MaterialTheme.typography.labelSmall, color = VerdeOscuro,
                        modifier = Modifier.align(Alignment.CenterHorizontally))
                    Text("$revisados", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                TarjetaContorno(modifier = Modifier.weight(1f), colorFondo = Blanco) {
                    Text("PENDIENTES", style = MaterialTheme.typography.labelSmall, color = GrisTexto,
                        modifier = Modifier.align(Alignment.CenterHorizontally))
                    Text("$pendientes", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally))
                }
            }

            // Próximos materiales por revisar
            if (proximos.isNotEmpty()) {
                Text("Próximos por revisar", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                proximos.forEach { material ->
                    TarjetaMaterialRelacionado(
                        material = material,
                        nombreAsignatura = DatosSimulados.buscarAsignatura(material.asignaturaId)?.nombre ?: "",
                        onClick = { onAbrirMaterial(material.id) }
                    )
                }
            }
        }
    }
}
