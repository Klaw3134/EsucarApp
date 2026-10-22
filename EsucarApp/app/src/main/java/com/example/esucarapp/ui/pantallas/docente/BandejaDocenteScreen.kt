package com.example.esucarapp.ui.pantallas.docente

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.esucarapp.data.DatosSimulados
import com.example.esucarapp.ui.componentes.*
import com.example.esucarapp.ui.theme.*

// Bandeja del docente con pestañas Todas / Pendientes / Respondidas
@Composable
fun BandejaDocenteScreen(
    usuario: String,
    onAbrirConsulta: (Int) -> Unit,
    onCerrarSesion: () -> Unit
) {
    var pestana by remember { mutableStateOf(0) }
    val total = DatosSimulados.consultas.size
    val pendientes = DatosSimulados.consultas.count { !it.respondida }
    val respondidas = total - pendientes
    val titulos = listOf("Todas · $total", "Pendientes · $pendientes", "Respondidas · $respondidas")
    val lista = DatosSimulados.consultasDocente(pestana)

    Scaffold(
        topBar = {
            BarraSuperior(
                "Bandeja del docente",
                acciones = { IconoPerfil(usuario, "Docente", onCerrarSesion) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Text(
                "Revisa y responde las consultas de tus estudiantes.",
                color = GrisTexto,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(8.dp))
            TabRow(selectedTabIndex = pestana, containerColor = Blanco, contentColor = Verde) {
                titulos.forEachIndexed { indice, texto ->
                    Tab(
                        selected = pestana == indice,
                        onClick = { pestana = indice },
                        text = {
                            Text(
                                texto,
                                fontWeight = if (pestana == indice) FontWeight.Bold else FontWeight.Normal,
                                style = MaterialTheme.typography.labelLarge
                            )
                        },
                        selectedContentColor = Verde,
                        unselectedContentColor = GrisTexto
                    )
                }
            }
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { EncabezadoSeccion("Consultas recibidas", "Más recientes") }
                if (lista.isEmpty()) {
                    item { Text("No hay consultas en esta pestaña.", color = GrisTexto) }
                }
                items(lista, key = { it.id }) { consulta ->
                    val asignatura = DatosSimulados.buscarAsignatura(consulta.asignaturaId)
                    TarjetaConsulta(
                        consulta = consulta,
                        encabezado = "${consulta.estudiante} · ${asignatura?.nombre ?: ""}",
                        onClick = { onAbrirConsulta(consulta.id) }
                    )
                }
            }
        }
    }
}
