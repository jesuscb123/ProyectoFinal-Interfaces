package dam2.jetpack.proyectofinal.user.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Summarize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dam2.jetpack.proyectofinal.events.presentation.state.EventUiState
import dam2.jetpack.proyectofinal.events.presentation.viewModel.EventViewModel
import dam2.jetpack.proyectofinal.user.domain.model.Rol
import dam2.jetpack.proyectofinal.user.domain.model.User
import dam2.jetpack.proyectofinal.user.presentation.viewmodel.UserViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
// NOTA: Asegúrate de que EventStatsChart está en el paquete correcto o impórtalo aquí
import dam2.jetpack.proyectofinal.core.components.navigation.EventStatsChart

/**
 * Pantalla de administración con dos secciones: Lista de usuarios y Estadísticas de eventos.
 *
 * Utiliza un [TabRow] para navegar entre las dos vistas.
 * - La pestaña "Usuarios" muestra una lista de todos los usuarios registrados.
 * - La pestaña "Estadísticas" muestra un gráfico con eventos completados vs. aceptados y
 *   permite generar un informe.
 *
 * @param userViewModel El ViewModel que gestiona el estado de los usuarios.
 * @param eventViewModel El ViewModel que gestiona el estado de los eventos y sus estadísticas.
 */
@Composable
fun AdminScreen(
    userViewModel: UserViewModel = hiltViewModel(),
    eventViewModel: EventViewModel = hiltViewModel()
) {
    val userState by userViewModel.uiState.collectAsState()
    val eventState by eventViewModel.uiState.collectAsState()

    // --- CAMBIO 3: Cargar los datos de ambos ViewModels ---
    LaunchedEffect(Unit) {
        userViewModel.loadUsers()
        eventViewModel.loadEventStats() // Cargar estadísticas de eventos
    }

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Usuarios", "Estadísticas")

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(text = title) }
                    )
                }
            }

            // --- CAMBIO 4: Usar el estado correcto para cada vista ---
            when (selectedTabIndex) {
                0 -> UserListContent(userState.users)
                1 -> StatsContent(
                    // Pasamos el estado de eventos
                    uiState = eventState,
                    onGenerateReport = {
                        // Usamos los datos del estado de eventos para el informe
                        val report = generateReport(eventState.completedEventsCount, eventState.acceptedEventsCount)
                        android.util.Log.i("AdminReport", report)
                    }
                )
            }
        }
    }
}

/**
 * Contenido para la pestaña de "Usuarios". (Sin cambios)
 */
@Composable
private fun UserListContent(users: List<User>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
    ) {
        if (users.isEmpty()) {
            EmptyState(
                title = "No hay usuarios",
                message = "Todavía no se han registrado usuarios en la aplicación."
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(users, key = { it.firebaseUid }) { user ->
                    UserListItem(user = user)
                }
            }
        }
    }
}

/**
 * Contenido para la pestaña de "Estadísticas".
 *
 * @param uiState El estado de la UI de EVENTOS que contiene las cuentas.
 * @param onGenerateReport Lambda que se ejecuta al pulsar el botón de informe.
 */
@Composable
private fun StatsContent(
    // --- CAMBIO 5: La firma de la función ahora espera EventUiState ---
    uiState: EventUiState,
    onGenerateReport: () -> Unit
) {
    // Si hay un error, lo mostramos
    if (uiState.errorMessage != null) {
        EmptyState(title = "Error", message = uiState.errorMessage)
        return
    }

    // Si está cargando, mostramos un indicador
    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Resumen de Eventos",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        // El gráfico ahora usa los datos de EventUiState
        EventStatsChart(
            completedCount = uiState.completedEventsCount,
            acceptedCount = uiState.acceptedEventsCount
        )

        Spacer(modifier = Modifier.weight(1f)) // Empuja el botón hacia abajo

        Button(
            onClick = onGenerateReport,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Summarize, contentDescription = "Generar Informe")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Generar Informe")
        }
    }
}

/**
 * Función para formatear el informe en un String. (Sin cambios)
 */
private fun generateReport(completed: Int, accepted: Int): String {
    val date = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
    val total = completed + accepted
    val completedPercentage = if (total > 0) (completed.toFloat() / total) * 100 else 0f

    return """
        ========================================
             INFORME DE ESTADO DE EVENTOS
        ========================================

        Fecha de generación: $date

        --- RESUMEN ---
        Eventos Completados:  $completed
        Eventos Aceptados:    $accepted
        ----------------------------------------
        Total de Eventos gestionados: $total

        --- ANÁLISIS ---
        - El ${String.format("%.1f", completedPercentage)}% de los eventos han sido completados.
        - Se observa una participación activa con $accepted eventos actualmente aceptados por los voluntarios.

        Este es un informe autogenerado por la aplicación.
    """.trimIndent()
}


// --- COMPOSABLES QUE YA TENÍAS (Sin cambios) ---
// UserListItem, AdminChip y EmptyState se mantienen igual.
// ... (El resto de tu código para UserListItem, AdminChip, EmptyState va aquí)
@Composable
fun UserListItem(
    user: User,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val icon = if (user.rol == Rol.ADMIN) Icons.Default.AdminPanelSettings else Icons.Default.Person
            Icon(
                imageVector = icon,
                contentDescription = "Rol de usuario",
                modifier = Modifier
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape)
                    .padding(8.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "UID: ${user.firebaseUid.substring(0, 10)}...", // Mostramos solo una parte del UID
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (user.rol == Rol.ADMIN) {
                Spacer(modifier = Modifier.width(16.dp))
                AdminChip()
            }
        }
    }
}

@Composable
fun AdminChip() {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        Text(
            text = "Admin",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun EmptyState(
    modifier: Modifier = Modifier,
    title: String,
    message: String
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}
