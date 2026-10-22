package com.example.esucarapp.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Slideshow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.esucarapp.data.Consulta
import com.example.esucarapp.data.Material
import com.example.esucarapp.ui.theme.*

// Map tipo de material
val iconoPorTipo: Map<String, ImageVector> = mapOf(
    "Guía" to Icons.Default.Description,
    "Lectura" to Icons.Default.Book,
    "Presentación" to Icons.Default.Slideshow,
    "Video" to Icons.Default.PlayCircle
)

fun iconoDeTipo(tipo: String): ImageVector = iconoPorTipo[tipo] ?: Icons.Default.Description

// Barra superior reutilizable
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperior(
    titulo: String,
    onVolver: (() -> Unit)? = null,
    acciones: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                titulo,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            if (onVolver != null) {
                IconButton(onClick = onVolver) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                }
            }
        },
        actions = acciones,
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Blanco)
    )
}

// Botón de perfil con menú para cerrar sesión
@Composable
fun IconoPerfil(nombre: String, rol: String, onCerrarSesion: () -> Unit) {
    var abierto by remember { mutableStateOf(false) }
    Box(modifier = Modifier.padding(end = 8.dp)) {
        IconButton(
            onClick = { abierto = true },
            modifier = Modifier
                .size(44.dp)
                .border(1.dp, Verde, CircleShape)
                .background(VerdeClaro.copy(alpha = 0.4f), CircleShape)
        ) {
            Icon(Icons.Default.Person, contentDescription = "Perfil", tint = Verde)
        }
        DropdownMenu(expanded = abierto, onDismissRequest = { abierto = false }) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Text(nombre, fontWeight = FontWeight.Bold)
                Text(rol, style = MaterialTheme.typography.bodySmall, color = GrisTexto)
            }
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text("Cerrar sesión") },
                leadingIcon = { Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null) },
                onClick = {
                    abierto = false
                    onCerrarSesion()
                }
            )
        }
    }
}

// Tarjeta con borde verde claro
@Composable
fun TarjetaContorno(
    modifier: Modifier = Modifier,
    colorBorde: Color = BordeVerde,
    colorFondo: Color = FondoTarjeta,
    onClick: (() -> Unit)? = null,
    contenido: @Composable ColumnScope.() -> Unit
) {
    val forma = RoundedCornerShape(12.dp)
    var m = modifier
        .fillMaxWidth()
        .clip(forma)
        .background(colorFondo)
        .border(1.dp, colorBorde, forma)
    if (onClick != null) {
        m = m.clickable { onClick() }
    }
    Column(modifier = m.padding(16.dp), content = contenido)
}

// Título grande + subtítulo de cada pantalla
@Composable
fun TituloPantalla(titulo: String, subtitulo: String? = null) {
    Column {
        Text(titulo, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        if (subtitulo != null) {
            Spacer(Modifier.height(4.dp))
            Text(subtitulo, style = MaterialTheme.typography.bodyMedium, color = GrisTexto)
        }
    }
}

@Composable
fun EncabezadoSeccion(titulo: String, detalle: String? = null) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        if (detalle != null) {
            Text(detalle, style = MaterialTheme.typography.bodySmall, color = GrisTexto)
        }
    }
}

// Etiqueta "Pendiente" / "Respondida"
@Composable
fun ChipEstado(respondida: Boolean) {
    val fondo = if (respondida) VerdeClaro else GrisChip
    val color = if (respondida) VerdeOscuro else GrisTexto
    Surface(shape = RoundedCornerShape(6.dp), color = fondo) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                if (respondida) Icons.Default.Check else Icons.Default.Schedule,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(14.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                if (respondida) "Respondida" else "Pendiente",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = color
            )
        }
    }
}

// Tarjeta de consulta para listas (estudiante y docente)
@Composable
fun TarjetaConsulta(consulta: Consulta, encabezado: String, onClick: () -> Unit) {
    TarjetaContorno(onClick = onClick) {
        Text(encabezado, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(6.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                consulta.asunto,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = GrisTexto)
        }
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                consulta.fecha,
                style = MaterialTheme.typography.bodySmall,
                color = GrisTexto,
                modifier = Modifier.weight(1f)
            )
            ChipEstado(consulta.respondida)
        }
    }
}

// "Guía relacionada"
@Composable
fun TarjetaMaterialRelacionado(material: Material, nombreAsignatura: String, onClick: () -> Unit) {
    TarjetaContorno(onClick = onClick) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(iconoDeTipo(material.tipo), contentDescription = null, tint = Verde)
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(material.titulo, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text(
                    "$nombreAsignatura · ${material.tipo}",
                    style = MaterialTheme.typography.bodySmall,
                    color = GrisTexto
                )
            }
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = GrisTexto)
        }
    }
}

// Fila con barra de progreso de una asignatura
@Composable
fun FilaProgreso(nombre: String, progreso: Float) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(nombre, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
            Text("${(progreso * 100).toInt()}%", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = { progreso },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = Verde,
            trackColor = VerdeClaro
        )
    }
}

// Mensaje cuando no se encuentra un dato (id inválido)
@Composable
fun MensajeNoEncontrado(texto: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(texto, color = GrisTexto)
    }
}
