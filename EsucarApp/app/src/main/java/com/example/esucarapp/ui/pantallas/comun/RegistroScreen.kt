package com.example.esucarapp.ui.pantallas.comun

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.esucarapp.data.local.Roles
import com.example.esucarapp.data.local.UsuarioDao
import com.example.esucarapp.data.local.UsuarioEntity
import com.example.esucarapp.data.local.encriptar
import com.example.esucarapp.ui.componentes.BarraSuperior
import com.example.esucarapp.ui.componentes.TituloPantalla
import com.example.esucarapp.ui.theme.GrisTexto
import kotlinx.coroutines.launch

// Reglas de validación del registro. Devuelve el error o null
fun validarRegistro(usuario: String, contrasena: String, confirmar: String): String? {
    return when {
        usuario.isBlank() || contrasena.isBlank() || confirmar.isBlank() -> "Completa todos los campos"
        usuario.trim().length < 4 -> "El usuario debe tener al menos 4 caracteres"
        usuario.trim().contains(" ") -> "El usuario no puede tener espacios"
        contrasena.length < 4 -> "La contraseña debe tener al menos 4 caracteres"
        contrasena != confirmar -> "Las contraseñas no coinciden"
        else -> null
    }
}

/*
 * Formulario de REGISTRO: guarda un nuevo usuario en SQLite (Room).
 * Después vuelve al login para que el usuario verifique sus credenciales.
 */
@Composable
fun RegistroScreen(dao: UsuarioDao, onVolver: () -> Unit) {
    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmar by remember { mutableStateOf("") }
    var rol by remember { mutableStateOf(Roles.ESTUDIANTE) }
    var mensajeError by remember { mutableStateOf("") }
    var guardando by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(topBar = { BarraSuperior("Crear cuenta", onVolver = onVolver) }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TituloPantalla("Registro de usuario", "Crea tu cuenta para acceder al repositorio ESUCAR.")
            Spacer(Modifier.height(4.dp))

            OutlinedTextField(
                value = usuario,
                onValueChange = { usuario = it; mensajeError = "" },
                label = { Text("Nombre de usuario") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it; mensajeError = "" },
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = confirmar,
                onValueChange = { confirmar = it; mensajeError = "" },
                label = { Text("Confirmar contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            Text("Tipo de usuario", style = MaterialTheme.typography.labelLarge)
            SelectorRol(rol) { rol = it }

            if (mensajeError.isNotBlank()) {
                Text(mensajeError, color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = {
                    val error = validarRegistro(usuario, contrasena, confirmar)
                    if (error != null) {
                        mensajeError = error
                    } else {
                        guardando = true
                        scope.launch {
                            val nombre = usuario.trim()
                            if (dao.buscarPorNombre(nombre) != null) {
                                mensajeError = "El usuario \"$nombre\" ya existe"
                            } else {
                                try {
                                    dao.insertarUsuario(
                                        UsuarioEntity(
                                            nombreUsuario = nombre,
                                            contrasena = encriptar(contrasena),
                                            rol = rol
                                        )
                                    )
                                    Toast.makeText(context, "Usuario registrado. Ahora inicia sesión.", Toast.LENGTH_LONG).show()
                                    onVolver()
                                } catch (e: Exception) {
                                    mensajeError = "No se pudo registrar el usuario"
                                }
                            }
                            guardando = false
                        }
                    }
                },
                enabled = !guardando,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(if (guardando) "Guardando..." else "Registrarme")
            }

            Text(
                "Tu contraseña se guarda encriptada en la base de datos local del teléfono.",
                style = MaterialTheme.typography.bodySmall,
                color = GrisTexto
            )
        }
    }
}
