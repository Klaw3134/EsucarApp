package com.example.esucarapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Base de datos local SQLite manejada por Room (mismo patrón de la guía de persistencia)
@Database(entities = [UsuarioEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun obtenerBaseDeDatos(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "esucar_local_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
