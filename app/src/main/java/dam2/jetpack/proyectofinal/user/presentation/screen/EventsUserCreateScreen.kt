package dam2.jetpack.proyectofinal.user.presentation.screen
import androidx.compose.animation.core.copy
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import dam2.jetpack.proyectofinal.events.presentation.viewModel.EventViewModel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import dam2.jetpack.proyectofinal.events.domain.model.Event

/**
 * Pantalla que muestra una lista de los eventos creados por el usuario actualmente autenticado.
 *
 * Esta pantalla gestiona varios estados:
 * - Muestra un indicador de carga mientras se obtienen los eventos.
 * - Muestra un mensaje si el usuario no ha creado ningún evento.
 * - Muestra una lista de eventos [EventItemCreated] si existen.
 * - Permite al usuario marcar un evento como "resuelto" a través de un diálogo de confirmación.
 *
 * @param eventViewModel El ViewModel que gestiona la lógica y el estado de los eventos.
 */
@Composable
fun EventsUserCreateScreen(
    eventViewModel: EventViewModel = hiltViewModel(),
){
    val eventState by eventViewModel.uiState.collectAsState()

    val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email

    var selectedEvent by remember { mutableStateOf<Event?>(null) }

    LaunchedEffect(currentUserEmail) {
        if (currentUserEmail != null) {
            eventViewModel.getEventsUserCreate(currentUserEmail)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            eventState.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            eventState.events.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Aún no has creado ningún evento.")
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(
                        items = eventState.events,
                        key = { it.eventId!! }
                    ) { event ->

                        EventItemCreated(
                            event = event,
                            onResolveClick = {
                                selectedEvent = event
                            }
                        )
                    }
                }
            }
        }

        selectedEvent?.let { event ->
            ResolveEventDialog(
                event = event,
                onDismiss = {
                    selectedEvent = null
                },
                onMarkAsResolved = {
                    eventViewModel.markEventAsResolved(it, 5)
                }
            )
        }
    }
}

/**
 * Composable que representa una tarjeta de evento en la lista de eventos creados por el usuario.
 *
 * Muestra los detalles del evento, como el título, la categoría, la fecha y la descripción.
 * Además, gestiona la visualización del estado del evento:
 * - "Resuelto": Muestra un chip indicando que el evento ha sido completado.
 * - "Aceptado": Muestra un chip y un botón para que el creador marque el evento como solucionado.
 * - "Pendiente": Indica que el evento aún no ha sido aceptado por otro usuario.
 *
 * @param event El objeto [Event] a mostrar.
 * @param onResolveClick Lambda que se invoca cuando el usuario pulsa el botón "MARCAR COMO SOLUCIONADO".
 */
@Composable
fun EventItemCreated(
    event: Event,
    onResolveClick: () -> Unit
) {
    val scheme = MaterialTheme.colorScheme

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = scheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column {

            // Header igual que Home (badge + título + chip a la derecha)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(scheme.primaryContainer)
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .background(scheme.secondaryContainer, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = event.categoria.toIcon(),
                            contentDescription = "Categoría",
                            tint = scheme.onSecondaryContainer,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = event.tituloEvento,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = scheme.onPrimaryContainer,
                            maxLines = 1
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "por ${event.userId.substringBefore('@')}",
                            style = MaterialTheme.typography.bodySmall,
                            color = scheme.onPrimaryContainer.copy(alpha = 0.85f),
                            maxLines = 1
                        )
                    }

                    // Chip de estado (arriba a la derecha)
                    when {
                        event.resuelto -> ResolvedChip()
                        event.userAccept != null -> InProgressChip() // aceptado, pendiente de resolver
                        else -> PendingChipV2() // nadie lo aceptó aún
                    }
                }
            }

            // Cuerpo
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Si tienes descripción en tu modelo, la mostramos (como en Home)
                Text(
                    text = event.descripcionEvento,
                    style = MaterialTheme.typography.bodyMedium,
                    color = scheme.onSurface,
                    maxLines = 2
                )

                Spacer(Modifier.height(12.dp))

                // Igual que Home: pills de info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    InfoPill(
                        icon = Icons.Default.CalendarMonth,
                        text = event.fechaCreacion.formatToString()
                    )
                    InfoPill(
                        icon = Icons.Default.Category,
                        text = event.categoria.name.lowercase().replaceFirstChar { it.uppercase() }
                    )
                }

                Spacer(Modifier.height(16.dp))

                // ✅ TU LÓGICA ORIGINAL, pero con UI más integrada
                when {
                    event.resuelto -> {
                        // Estado solucionado (con colores del tema)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(scheme.tertiaryContainer, RoundedCornerShape(16.dp))
                                .padding(horizontal = 12.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = "Solucionado",
                                tint = scheme.onTertiaryContainer
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "SOLUCIONADO",
                                color = scheme.onTertiaryContainer,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    event.userAccept != null -> {
                        // Botón marcar como solucionado (mismo theme)
                        Button(
                            onClick = onResolveClick,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("MARCAR COMO SOLUCIONADO")
                        }
                    }

                    else -> {
                        // Pendiente de aceptación
                        Text(
                            text = "Pendiente de aceptación por otro usuario",
                            style = MaterialTheme.typography.bodySmall,
                            color = scheme.onSurfaceVariant,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Chip visual que indica que un evento ha sido "RESUELTO".
 *
 * Muestra un icono de verificación y el texto "RESUELTO" con un estilo distintivo.
 */
@Composable
private fun ResolvedChip() {
    val scheme = MaterialTheme.colorScheme
    Card(
        colors = CardDefaults.cardColors(containerColor = scheme.tertiaryContainer),
        shape = RoundedCornerShape(50),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = scheme.onTertiaryContainer,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "RESUELTO",
                color = scheme.onTertiaryContainer,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * Chip visual que indica que un evento ha sido "ACEPTADO" por otro usuario.
 *
 * Muestra un icono de tarea y el texto "ACEPTADO", indicando que está en progreso.
 */
@Composable
private fun InProgressChip() {
    val scheme = MaterialTheme.colorScheme
    Card(
        colors = CardDefaults.cardColors(containerColor = scheme.primaryContainer),
        shape = RoundedCornerShape(50),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.TaskAlt,
                contentDescription = null,
                tint = scheme.onPrimaryContainer,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "ACEPTADO",
                color = scheme.onPrimaryContainer,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
