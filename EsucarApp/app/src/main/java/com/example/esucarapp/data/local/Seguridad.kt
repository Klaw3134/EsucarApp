package com.example.esucarapp.data.local

import java.security.MessageDigest

// Convierte la contraseña a un hash SHA-256 antes de guardarla o compararla.
// Así en la base de datos nunca queda la contraseña real.
fun encriptar(texto: String): String {
    val bytes = MessageDigest.getInstance("SHA-256").digest(texto.toByteArray())
    return bytes.joinToString("") { "%02x".format(it) }
}
