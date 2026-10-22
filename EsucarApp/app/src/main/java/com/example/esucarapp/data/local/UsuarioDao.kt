package com.example.esucarapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

// Operaciones SQL sobre la tabla "usuarios"
@Dao
interface UsuarioDao {

    // Registro de un usuario nuevo. ABORT = lanza error si el nombre ya existe.
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertarUsuario(usuario: UsuarioEntity): Long

    // Busca un usuario por nombre (para saber si ya existe al registrar)
    @Query("SELECT * FROM usuarios WHERE nombreUsuario = :nombre LIMIT 1")
    suspend fun buscarPorNombre(nombre: String): UsuarioEntity?

    // Verifica nombre + contraseña (login)
    @Query("SELECT * FROM usuarios WHERE nombreUsuario = :nombre AND contrasena = :contrasena LIMIT 1")
    suspend fun validarLogin(nombre: String, contrasena: String): UsuarioEntity?

    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun contarUsuarios(): Int
}
