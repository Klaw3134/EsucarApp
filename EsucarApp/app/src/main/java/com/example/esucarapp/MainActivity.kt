package com.example.esucarapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.esucarapp.data.local.AppDatabase
import com.example.esucarapp.data.local.Roles
import com.example.esucarapp.data.local.UsuarioEntity
import com.example.esucarapp.data.local.encriptar
import com.example.esucarapp.navegacion.AppNavegacion
import com.example.esucarapp.ui.theme.EsucarTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inicializar Room (SQLite local), igual que en la guía de persistencia
        val database = AppDatabase.obtenerBaseDeDatos(applicationContext)
        val dao = database.usuarioDao()

        // Primera vez: se crean dos usuarios de prueba (uno por rol)
        lifecycleScope.launch {
            if (dao.contarUsuarios() == 0) {
                dao.insertarUsuario(UsuarioEntity(nombreUsuario = "pepito", contrasena = encriptar("1234"), rol = Roles.ESTUDIANTE))
                dao.insertarUsuario(UsuarioEntity(nombreUsuario = "profesor", contrasena = encriptar("1234"), rol = Roles.DOCENTE))
            }
        }

        setContent {
            EsucarTheme {
                AppNavegacion(dao = dao)
            }
        }
    }
}
