package com.example.esucarapp.navegacion

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.esucarapp.data.local.Roles
import com.example.esucarapp.data.local.UsuarioDao
import com.example.esucarapp.ui.pantallas.comun.LoginScreen
import com.example.esucarapp.ui.pantallas.comun.RegistroScreen
import com.example.esucarapp.ui.pantallas.docente.BandejaDocenteScreen
import com.example.esucarapp.ui.pantallas.docente.InicioDocenteScreen
import com.example.esucarapp.ui.pantallas.docente.PublicarMaterialScreen
import com.example.esucarapp.ui.pantallas.docente.ResponderConsultaScreen
import com.example.esucarapp.ui.pantallas.estudiante.*
import com.example.esucarapp.ui.theme.*

// barra inferior
data class ItemMenu(val ruta: String, val etiqueta: String, val icono: ImageVector)


val menuEstudiante = listOf(
    ItemMenu(Rutas.EST_INICIO, "Asignaturas", Icons.Default.GridView),
    ItemMenu(Rutas.EST_PROGRESO, "Progreso", Icons.AutoMirrored.Filled.TrendingUp),
    ItemMenu(Rutas.EST_CONSULTAS, "Consultas", Icons.Default.ChatBubbleOutline)
)
val menuDocente = listOf(
    ItemMenu(Rutas.DOC_INICIO, "Asignaturas", Icons.Default.GridView),
    ItemMenu(Rutas.DOC_BANDEJA, "Consultas", Icons.Default.ChatBubbleOutline)
)

private fun NavBackStackEntry.idArg(nombre: String): Int =
    arguments?.getString(nombre)?.toIntOrNull() ?: 0


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AppNavegacion(dao: UsuarioDao) {
    val navController = rememberNavController()
    var nombreSesion by rememberSaveable { mutableStateOf("") }
    var rolSesion by rememberSaveable { mutableStateOf("") }

    val entradaActual by navController.currentBackStackEntryAsState()
    val rutaActual = entradaActual?.destination?.route
    val itemsMenu = if (rolSesion == Roles.DOCENTE) menuDocente else menuEstudiante
    val mostrarBarra = rolSesion.isNotBlank() && itemsMenu.any { it.ruta == rutaActual }

    fun cerrarSesion() {
        nombreSesion = ""
        rolSesion = ""
        navController.navigate(Rutas.LOGIN) {
            popUpTo(navController.graph.id) { inclusive = true }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (mostrarBarra) {
                BarraInferior(itemsMenu, rutaActual) { ruta ->
                    navController.navigate(ruta) {
                        popUpTo(itemsMenu.first().ruta) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Rutas.LOGIN,
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {
            // comunes
            composable(Rutas.LOGIN) {
                LoginScreen(
                    dao = dao,
                    onLoginExitoso = { usuario ->
                        nombreSesion = usuario.nombreUsuario
                        rolSesion = usuario.rol
                        // Redirección según el tipo de usuario
                        val destino = if (usuario.rol == Roles.DOCENTE) Rutas.DOC_INICIO else Rutas.EST_INICIO
                        navController.navigate(destino) {
                            popUpTo(Rutas.LOGIN) { inclusive = true }
                        }
                    },
                    onIrARegistro = { navController.navigate(Rutas.REGISTRO) }
                )
            }
            composable(Rutas.REGISTRO) {
                RegistroScreen(dao = dao, onVolver = { navController.popBackStack() })
            }
            composable(Rutas.VISOR) { entrada ->
                RutaProtegida(rolSesion.isNotBlank(), onDenegado = { cerrarSesion() }) {
                    VisorMaterialScreen(
                        materialId = entrada.idArg("materialId"),
                        usuario = nombreSesion,
                        soloLectura = rolSesion == Roles.DOCENTE,
                        onVolver = { navController.popBackStack() }
                    )
                }
            }

            composable(Rutas.EST_INICIO) {
                RutaProtegida(rolSesion == Roles.ESTUDIANTE, onDenegado = { cerrarSesion() }) {
                    InicioEstudianteScreen(
                        usuario = nombreSesion,
                        onAbrirAsignatura = { id -> navController.navigate(Rutas.estAsignatura(id)) },
                        onCerrarSesion = { cerrarSesion() }
                    )
                }
            }
            composable(Rutas.EST_ASIGNATURA) { entrada ->
                RutaProtegida(rolSesion == Roles.ESTUDIANTE, onDenegado = { cerrarSesion() }) {
                    AsignaturaScreen(
                        asignaturaId = entrada.idArg("asignaturaId"),
                        usuario = nombreSesion,
                        esDocente = false,
                        onVolver = { navController.popBackStack() },
                        onAbrirMaterial = { id -> navController.navigate(Rutas.estMaterial(id)) }
                    )
                }
            }
            composable(Rutas.EST_MATERIAL) { entrada ->
                RutaProtegida(rolSesion == Roles.ESTUDIANTE, onDenegado = { cerrarSesion() }) {
                    MaterialScreen(
                        materialId = entrada.idArg("materialId"),
                        usuario = nombreSesion,
                        onVolver = { navController.popBackStack() },
                        onVer = { id -> navController.navigate(Rutas.visor(id)) }
                    )
                }
            }
            composable(Rutas.EST_PROGRESO) {
                RutaProtegida(rolSesion == Roles.ESTUDIANTE, onDenegado = { cerrarSesion() }) {
                    ProgresoScreen(
                        usuario = nombreSesion,
                        onAbrirMaterial = { id -> navController.navigate(Rutas.estMaterial(id)) }
                    )
                }
            }
            composable(Rutas.EST_CONSULTAS) {
                RutaProtegida(rolSesion == Roles.ESTUDIANTE, onDenegado = { cerrarSesion() }) {
                    MisConsultasScreen(
                        usuario = nombreSesion,
                        onAbrirConsulta = { id -> navController.navigate(Rutas.estDetalleConsulta(id)) }
                    )
                }
            }
            composable(Rutas.EST_DETALLE_CONSULTA) { entrada ->
                RutaProtegida(rolSesion == Roles.ESTUDIANTE, onDenegado = { cerrarSesion() }) {
                    DetalleConsultaScreen(
                        consultaId = entrada.idArg("consultaId"),
                        onVolver = { navController.popBackStack() },
                        onAbrirMaterial = { id -> navController.navigate(Rutas.estMaterial(id)) }
                    )
                }
            }
            composable(Rutas.DOC_INICIO) {
                RutaProtegida(rolSesion == Roles.DOCENTE, onDenegado = { cerrarSesion() }) {
                    InicioDocenteScreen(
                        usuario = nombreSesion,
                        onAbrirAsignatura = { id -> navController.navigate(Rutas.docAsignatura(id)) },
                        onCerrarSesion = { cerrarSesion() }
                    )
                }
            }
            composable(Rutas.DOC_ASIGNATURA) { entrada ->
                val asignaturaId = entrada.idArg("asignaturaId")
                RutaProtegida(rolSesion == Roles.DOCENTE, onDenegado = { cerrarSesion() }) {
                    AsignaturaScreen(
                        asignaturaId = asignaturaId,
                        usuario = nombreSesion,
                        esDocente = true,
                        onVolver = { navController.popBackStack() },
                        onAbrirMaterial = { id -> navController.navigate(Rutas.visor(id)) },
                        onPublicar = { navController.navigate(Rutas.docPublicar(asignaturaId)) }
                    )
                }
            }
            composable(Rutas.DOC_PUBLICAR) { entrada ->
                RutaProtegida(rolSesion == Roles.DOCENTE, onDenegado = { cerrarSesion() }) {
                    PublicarMaterialScreen(
                        asignaturaId = entrada.idArg("asignaturaId"),
                        onVolver = { navController.popBackStack() }
                    )
                }
            }
            composable(Rutas.DOC_BANDEJA) {
                RutaProtegida(rolSesion == Roles.DOCENTE, onDenegado = { cerrarSesion() }) {
                    BandejaDocenteScreen(
                        usuario = nombreSesion,
                        onAbrirConsulta = { id -> navController.navigate(Rutas.docResponder(id)) },
                        onCerrarSesion = { cerrarSesion() }
                    )
                }
            }
            composable(Rutas.DOC_RESPONDER) { entrada ->
                RutaProtegida(rolSesion == Roles.DOCENTE, onDenegado = { cerrarSesion() }) {
                    ResponderConsultaScreen(
                        consultaId = entrada.idArg("consultaId"),
                        onVolver = { navController.popBackStack() },
                        onAbrirMaterial = { id -> navController.navigate(Rutas.visor(id)) }
                    )
                }
            }
        }
    }
}

// Muestra la pantalla solo si el rol tiene permiso; si no, ejecuta onDenegado (vuelve al login)
@Composable
fun RutaProtegida(permitido: Boolean, onDenegado: () -> Unit, contenido: @Composable () -> Unit) {
    if (permitido) {
        contenido()
    } else {
        LaunchedEffect(Unit) { onDenegado() }
    }
}

// Barra inferior (Scaffold bottomBar) con los ítems del rol actual
@Composable
fun BarraInferior(items: List<ItemMenu>, rutaActual: String?, onNavegar: (String) -> Unit) {
    NavigationBar(containerColor = Blanco) {
        items.forEach { item ->
            NavigationBarItem(
                selected = rutaActual == item.ruta,
                onClick = { onNavegar(item.ruta) },
                icon = { Icon(item.icono, contentDescription = item.etiqueta) },
                label = { Text(item.etiqueta) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Verde,
                    selectedTextColor = Verde,
                    indicatorColor = VerdeClaro,
                    unselectedIconColor = Verde,
                    unselectedTextColor = GrisTexto
                )
            )
        }
    }
}
