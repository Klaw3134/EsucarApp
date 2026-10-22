package com.example.esucarapp.ui.pantallas.comun

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.esucarapp.data.local.Roles
import com.example.esucarapp.data.local.UsuarioDao
import com.example.esucarapp.data.local.UsuarioEntity
import com.example.esucarapp.data.local.encriptar
import com.example.esucarapp.ui.componentes.BarraSuperior
import com.example.esucarapp.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    dao: UsuarioDao,
    onLoginExitoso: (UsuarioEntity) -> Unit,
    onIrARegistro: () -> Unit
) {
    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var rolSeleccionado by remember { mutableStateOf(Roles.ESTUDIANTE) }
    var verContrasena by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Scaffold(topBar = { BarraSuperior("Acceso") }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))
            // Logo
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(FondoTarjeta, RoundedCornerShape(16.dp))
                    .border(1.dp, GrisBorde, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Shield, contentDescription = null, tint = Verde, modifier = Modifier.size(36.dp))
            }
            Text("ESUCAR", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(
                "Escuela de Suboficiales de Carabineros",
                style = MaterialTheme.typography.bodySmall,
                color = GrisTexto,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = usuario,
                onValueChange = { usuario = it; mensajeError = "" },
                label = { Text("Usuario") },
                placeholder = { Text("Ingrese su nombre de usuario") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it; mensajeError = "" },
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = if (verContrasena) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { verContrasena = !verContrasena }) {
                        Icon(
                            if (verContrasena) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Mostrar contraseña"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                "Rol de Usuario",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.fillMaxWidth()
            )
            SelectorRol(rolSeleccionado) { rolSeleccionado = it; mensajeError = "" }

            if (mensajeError.isNotBlank()) {
                Text(mensajeError, color = MaterialTheme.colorScheme.error, modifier = Modifier.fillMaxWidth())
            }

            Button(
                onClick = {
                    if (usuario.isBlank() || contrasena.isBlank()) {
                        mensajeError = "Completa usuario y contraseña"
                    } else {
                        cargando = true
                        scope.launch {
                            // Room ejecuta las funciones suspend fuera del hilo principal
                            val encontrado = dao.validarLogin(usuario.trim(), encriptar(contrasena))
                            cargando = false
                            when {
                                encontrado == null ->
                                    mensajeError = "Usuario o contraseña incorrectos"
                                encontrado.rol != rolSeleccionado ->
                                    mensajeError = "Este usuario está registrado como ${Roles.nombreVisible(encontrado.rol)}"
                                else -> onLoginExitoso(encontrado)
                            }
                        }
                    }
                },
                enabled = !cargando,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(if (cargando) "Verificando..." else "Ingresar")
            }

            TextButton(onClick = onIrARegistro) {
                Text("¿No tienes cuenta? Regístrate")
            }

        }
    }
}
