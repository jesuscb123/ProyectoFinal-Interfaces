package dam2.jetpack.proyectofinal.user.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn

import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import dam2.jetpack.proyectofinal.events.domain.model.Category // Asegúrate que el import es correcto
import dam2.jetpack.proyectofinal.events.domain.model.Event
import dam2.jetpack.proyectofinal.events.presentation.viewModel.EventViewModel
import dam2.jetpack.proyectofinal.user.presentation.viewmodel.UserViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(
    onMyEventsClick: () -> Unit,
    userViewModel: UserViewModel = hiltViewModel(),
    eventViewModel: EventViewModel = hiltViewModel()
) {
    val userState by userViewModel.uiState.collectAsState()
    val eventState by eventViewModel.uiState.collectAsState()
    var selectedEvent by remember { mutableStateOf<Event?>(null) }

    LaunchedEffect(Unit) {
        FirebaseAuth.getInstance().currentUser?.uid?.let {
            userViewModel.getUserByFirebaseUid(it)
        }
        eventViewModel.loadEvents()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
        ) {

            if (eventState.events.isEmpty()) {
                EmptyState()
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(
                        items = eventState.events,
                        key = { it.eventId!! }
                    ) { event ->
                        var isVisible by remember { mutableStateOf(false) }
                        LaunchedEffect(key1 = true) { isVisible = true }

                        AnimatedVisibility(
                            visible = isVisible,
                            enter = fadeIn(animationSpec = tween(durationMillis = 500)) +
                                    slideInVertically(
                                        initialOffsetY = { it / 2 },
                                        animationSpec = tween(durationMillis = 500)
                                    )
                        ) {
                            EventItem(
                                event = event,
                                currentUserEmail = userState.user?.email,
                                onClick = {
                                    selectedEvent = event
                                }
                            )
                        }
                    }
                }
            }
        }

        selectedEvent?.let { event ->
            val currentUserEmail = userState.user?.email
            val isAcceptedByCurrentUser = event.userAccept == currentUserEmail

            AlertDialog(
                onDismissRequest = { selectedEvent = null },
                title = { Text(if (isAcceptedByCurrentUser) "Cancelar Asistencia" else "Aceptar Evento") },
                text = { Text("¿Qué deseas hacer con el evento \"${event.tituloEvento}\"?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (isAcceptedByCurrentUser) {
                                eventViewModel.cancelAcceptance(event)
                            } else {
                                currentUserEmail?.let { email ->
                                    eventViewModel.acceptEvent(event, email)
                                }
                            }
                            selectedEvent = null
                        }
                    ) {
                        Text(if (isAcceptedByCurrentUser) "Confirmar Cancelación" else "Aceptar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { selectedEvent = null }) {
                        Text("Cerrar")
                    }
                }
            )
        }
    }
}

@Composable
fun EventItem(
    event: Event,
    currentUserEmail: String?,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                enabled = event.userAccept == null || event.userAccept == currentUserEmail
            ) { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono de categoría
            Icon(
                imageVector = event.categoria.toIcon(),
                contentDescription = "Categoría",
                modifier = Modifier
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape)
                    .padding(8.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Contenido del evento
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = event.tituloEvento,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Creado por: ${event.userId.substringBefore('@')}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))

                // 1. AQUÍ AÑADIMOS LA FECHA
                Text(
                    text = event.fechaCreacion.formatToString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Estado del evento (Pendiente o Aceptado)
            if (event.userAccept != null) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Aceptado por",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = if (event.userAccept != currentUserEmail) "Ti" else event.userAccept.substringBefore('@'),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                StatusChip()
            }
        }
    }
}

// COMPOSABLE PARA LA "PASTILLA" DE ESTADO
@Composable
fun StatusChip() {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.errorContainer, RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = "PENDIENTE",
            color = MaterialTheme.colorScheme.onErrorContainer,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.EventBusy,
            contentDescription = "No hay eventos",
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Todo tranquilo por aquí",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "No hay eventos disponibles en este momento. ¡Vuelve a intentarlo más tarde!",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

fun Category.toIcon() = when (this) {
    Category.COMUNIDAD -> Icons.Default.Groups
    Category.PERSONAL -> Icons.Default.Person
}

fun Date.formatToString(): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return formatter.format(this)
}
