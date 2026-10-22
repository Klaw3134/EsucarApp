package com.example.esucarapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val EsquemaEsucar = lightColorScheme(
    primary = Verde,
    onPrimary = Blanco,
    primaryContainer = VerdeClaro,
    onPrimaryContainer = VerdeOscuro,
    secondary = VerdeOscuro,
    onSecondary = Blanco,
    secondaryContainer = VerdeClaro,
    onSecondaryContainer = VerdeOscuro,
    background = Blanco,
    onBackground = TextoPrincipal,
    surface = Blanco,
    onSurface = TextoPrincipal,
    surfaceVariant = FondoTarjeta,
    onSurfaceVariant = GrisTexto,
    surfaceContainer = Blanco,
    surfaceContainerHigh = Blanco,
    outline = GrisBorde,
    error = RojoError
)

@Composable
fun EsucarTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaEsucar,
        content = content
    )
}
