package com.example.esucarapp.ui.pantallas.docente

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.esucarapp.data.DatosSimulados
import com.example.esucarapp.ui.componentes.*
import com.example.esucarapp.ui.theme.*

// Pantalla principal del docente: sus asignaturas, materiales y consultas pendientes
@Composable
fun InicioDocenteScreen(
    usuario: String,
    onAbrirAsignatura: (Int) -> Unit,
    onCerrarSesion: () -> Unit
) {
    Scaffold(
        topBar = {
            BarraSuperior(
                titulo = "Hola, $usuario",
                acciones = { IconoPerfil(usuario, "Docente", onCerrarSesion) }
            )
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
                Text("Gestiona el material por unidad y revisa las consultas de tus estudiantes.", color = GrisTexto)
                Spacer(Modifier.height(12.dp))
                EncabezadoSeccion("Mis Asignaturas")
            }
            items(DatosSimulados.asignaturas, key = { it.id }) { asignatura ->
                val totalMateriales = DatosSimulados.materialesDe(asignatura.id).size
                val pendientes = DatosSimulados.pendientesDeAsignatura(asignatura.id)
                TarjetaContorno(onClick = { onAbrirAsignatura(asignatura.id) }) {
                    Text(asignatura.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(asignatura.codigo, style = MaterialTheme.typography.bodySmall, color = GrisTexto)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "$totalMateriales materiales · ${asignatura.unidades.size} unidades",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    if (pendientes > 0) {
                        Spacer(Modifier.height(6.dp))
                        ChipEstado(respondida = false)
                        Text(
                            "$pendientes consulta(s) por responder",
                            style = MaterialTheme.typography.bodySmall,
                            color = GrisTexto
                        )
                    }
                }
            }
        }
    }
}
