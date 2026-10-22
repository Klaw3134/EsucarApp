package com.example.esucarapp.ui.pantallas.docente

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.esucarapp.data.DatosSimulados
import com.example.esucarapp.ui.componentes.*
import com.example.esucarapp.ui.theme.*

/*
 * Formulario del docente para publicar material en una unidad.
 * Sin backend: el material se agrega a la lista en memoria y aparece de inmediato.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PublicarMaterialScreen(asignaturaId: Int, onVolver: () -> Unit) {
    val asignatura = DatosSimulados.buscarAsignatura(asignaturaId)
    if (asignatura == null) {
        MensajeNoEncontrado("Asignatura no encontrada")
        return
    }
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var unidad by remember { mutableStateOf(1) }
    var tipo by remember { mutableStateOf(DatosSimulados.tiposMaterial.first()) }
    var mensajeError by remember { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold(topBar = { BarraSuperior("Publicar material", onVolver = onVolver) }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TituloPantalla(asignatura.nombre, "${asignatura.codigo} · Nuevo recurso de estudio")

            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it; mensajeError = "" },
                label = { Text("Título del recurso") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Text("Unidad temática", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
            asignatura.unidades.forEach { u ->
                FilterChip(
                    selected = unidad == u.numero,
                    onClick = { unidad = u.numero },
                    label = { Text("Unidad ${u.numero}: ${u.titulo}") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = VerdeClaro,
                        selectedLabelColor = VerdeOscuro
                    )
                )
            }

            Text("Tipo de archivo", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DatosSimulados.tiposMaterial.forEach { t ->
                    FilterChip(
                        selected = tipo == t,
                        onClick = { tipo = t },
                        label = { Text(t) },
                        leadingIcon = { Icon(iconoDeTipo(t), contentDescription = null, modifier = Modifier.size(18.dp)) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = VerdeClaro,
                            selectedLabelColor = VerdeOscuro
                        )
                    )
                }
            }

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text(if (tipo == "Video") "Enlace o descripción" else "Descripción (opcional)") },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            if (mensajeError.isNotBlank()) {
                Text(mensajeError, color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = {
                    if (titulo.trim().length < 4) {
                        mensajeError = "Ingresa un título de al menos 4 caracteres"
                    } else {
                        DatosSimulados.publicarMaterial(asignatura.id, unidad, titulo.trim(), tipo, descripcion.trim())
                        Toast.makeText(context, "Material publicado en la Unidad $unidad", Toast.LENGTH_SHORT).show()
                        onVolver()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Publicar material")
            }
        }
    }
}
