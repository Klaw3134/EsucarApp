package com.example.esucarapp.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

// Tabla "usuarios" en SQLite (Room).
// nombreUsuario es único: no se pueden registrar dos usuarios con el mismo nombre.
@Entity(
    tableName = "usuarios",
    indices = [Index(value = ["nombreUsuario"], unique = true)]
)
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombreUsuario: String,
    val contrasena: String,   // se guarda encriptada (SHA-256), nunca en texto plano
    val rol: String           // Roles.ESTUDIANTE o Roles.DOCENTE
)

// Tipos de usuario de la app
object Roles {
    const val ESTUDIANTE = "ESTUDIANTE"
    const val DOCENTE = "DOCENTE"

    fun nombreVisible(rol: String): String =
        if (rol == DOCENTE) "Docente" else "Estudiante"
}
