package com.example.esucarapp.ui.pantallas.estudiante

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

// Pantalla principal del estudiante: lista de asignaturas con su avance
@Composable
fun InicioEstudianteScreen(
    usuario: String,
    onAbrirAsignatura: (Int) -> Unit,
    onCerrarSesion: () -> Unit
) {
    Scaffold(
        topBar = {
            BarraSuperior(
                titulo = "Hola, $usuario",
                acciones = { IconoPerfil(usuario, "Estudiante", onCerrarSesion) }
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
                // Resumen rápido del avance general
                TarjetaContorno(colorFondo = VerdeClaro.copy(alpha = 0.5f)) {
                    Text("Tu avance general", style = MaterialTheme.typography.bodyMedium, color = VerdeOscuro)
                    Text(
                        "${(DatosSimulados.progresoGeneral(usuario) * 100).toInt()}% completado",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = VerdeOscuro
                    )
                }
            }
            item {
                Spacer(Modifier.height(4.dp))
                EncabezadoSeccion("Mis Asignaturas", "${DatosSimulados.asignaturas.size} asignaturas")
            }
            items(DatosSimulados.asignaturas, key = { it.id }) { asignatura ->
                val progreso = DatosSimulados.progresoAsignatura(usuario, asignatura.id)
                TarjetaContorno(onClick = { onAbrirAsignatura(asignatura.id) }) {
                    Text(asignatura.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(
                        "${asignatura.codigo} · ${asignatura.docente}",
                        style = MaterialTheme.typography.bodySmall,
                        color = GrisTexto
                    )
                    Spacer(Modifier.height(10.dp))
                    FilaProgreso("Progreso de estudio", progreso)
                }
            }
        }
    }
}
