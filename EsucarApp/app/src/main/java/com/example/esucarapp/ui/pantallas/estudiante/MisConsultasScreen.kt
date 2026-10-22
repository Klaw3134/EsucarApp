package com.example.esucarapp.ui.pantallas.estudiante

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.esucarapp.data.DatosSimulados
import com.example.esucarapp.ui.componentes.*
import com.example.esucarapp.ui.theme.GrisTexto

// Lista de consultas enviadas por el estudiante
@Composable
fun MisConsultasScreen(usuario: String, onAbrirConsulta: (Int) -> Unit) {
    val misConsultas = DatosSimulados.consultasDe(usuario)
    val pendientes = misConsultas.count { !it.respondida }

    Scaffold(topBar = { BarraSuperior("Mis consultas") }) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("Revisa tus consultas y las respuestas del docente.", color = GrisTexto)
                Spacer(Modifier.height(12.dp))
                EncabezadoSeccion("Consultas enviadas", "$pendientes pendientes · ${misConsultas.size} en total")
            }
            if (misConsultas.isEmpty()) {
                item {
                    Text(
                        "Aún no has enviado consultas.",
                        color = GrisTexto,
                        modifier = Modifier.padding(vertical = 24.dp)
                    )
                }
            }
            items(misConsultas, key = { it.id }) { consulta ->
                TarjetaConsulta(
                    consulta = consulta,
                    encabezado = DatosSimulados.buscarAsignatura(consulta.asignaturaId)?.nombre ?: "",
                    onClick = { onAbrirConsulta(consulta.id) }
                )
            }
            item {
                Text(
                    "Para enviar una nueva consulta, ingresa al material de tu asignatura y completa «Consultar al Docente».",
                    style = MaterialTheme.typography.bodySmall,
                    color = GrisTexto
                )
            }
        }
    }
}
