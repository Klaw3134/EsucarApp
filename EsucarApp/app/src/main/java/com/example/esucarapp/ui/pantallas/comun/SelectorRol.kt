package com.example.esucarapp.ui.pantallas.comun

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxWidth
import com.example.esucarapp.data.local.Roles
import com.example.esucarapp.ui.theme.*

// Selector Estudiante / Docente (usado en Login y Registro)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorRol(rolSeleccionado: String, onCambiar: (String) -> Unit) {
    val opciones = listOf(Roles.ESTUDIANTE, Roles.DOCENTE)
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        opciones.forEachIndexed { indice, rol ->
            SegmentedButton(
                selected = rolSeleccionado == rol,
                onClick = { onCambiar(rol) },
                shape = SegmentedButtonDefaults.itemShape(index = indice, count = opciones.size),
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = VerdeClaro,
                    activeContentColor = VerdeOscuro
                )
            ) {
                Text(Roles.nombreVisible(rol))
            }
        }
    }
}
