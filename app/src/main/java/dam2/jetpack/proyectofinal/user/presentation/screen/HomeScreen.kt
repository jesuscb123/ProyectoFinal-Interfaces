package dam2.jetpack.proyectofinal.user.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.firebase.auth.FirebaseAuth
import dam2.jetpack.proyectofinal.events.domain.model.Category
import dam2.jetpack.proyectofinal.events.domain.model.Event
import dam2.jetpack.proyectofinal.events.presentation.viewModel.EventViewModel
import dam2.jetpack.proyectofinal.user.domain.model.Rol
import dam2.jetpack.proyectofinal.user.presentation.viewmodel.UserViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onMyEventsClick: () -> Unit,
    userViewModel: UserViewModel = hiltViewModel(),
    eventViewModel: EventViewModel = hiltViewModel()
) {
    val userState by userViewModel.uiState.collectAsState()
    val eventState by eventViewModel.uiState.collectAsState()
    var selectedEvent by remember { mutableStateOf<Event?>(null) }

    val isAdmin = userState.user?.rol == Rol.ADMIN

    // ✅ CLAVE: cada vez que vuelves a la pantalla, incrementamos esto
    // y así se recrean los SwipeState de los items (se “desbloquean”).
    var swipeResetKey by remember { mutableIntStateOf(0) }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                swipeResetKey++
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

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
                                    ),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            SwipeableEventItem(
                                event = event,
                                currentUserEmail = userState.user?.email,
                                isAdmin = isAdmin,
                                onDelete = { eventToDelete ->
                                    eventViewModel.deleteEvent(eventToDelete.eventId!!)
                                },
                                onClick = {
                                    if (event.userId != userState.user?.email) {
                                        selectedEvent = event
                                    }
                                },
                                resetKey = swipeResetKey // ✅ se lo pasamos al item
                            )
                        }
                    }
                }
            }
        }

        selectedEvent?.let { event ->
            if (event.userId != userState.user?.email) {
                EventActionDialog(
                    event = event,
                    currentUserEmail = userState.user?.email,
                    onDismiss = { selectedEvent = null },
                    onAccept = { evt, email ->
                        eventViewModel.acceptEvent(evt, email)
                    },
                    onCancelAcceptance = { evt ->
                        eventViewModel.cancelAcceptance(evt)
                    }
                )
            } else {
                LaunchedEffect(Unit) {
                    selectedEvent = null
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeableEventItem(
    event: Event,
    currentUserEmail: String?,
    isAdmin: Boolean,
    onDelete: (Event) -> Unit,
    onClick: () -> Unit,
    resetKey: Int // ✅ nuevo
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    key(resetKey, event.eventId) {

        var showDeleteDialog by remember { mutableStateOf(false) }
        var resetSwipe by remember { mutableStateOf(false) }

        val dismissState = rememberSwipeToDismissBoxState(
            confirmValueChange = { dismissValue ->
                if (dismissValue == SwipeToDismissBoxValue.EndToStart && isAdmin) {
                    showDeleteDialog = true
                    resetSwipe = true  // 👈 pedimos reset visual
                    false
                } else {
                    false
                }
            },
            positionalThreshold = { it * 0.25f }
        )

        LaunchedEffect(resetSwipe) {
            if (resetSwipe) {
                dismissState.snapTo(SwipeToDismissBoxValue.Settled)
                resetSwipe = false
            }
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDeleteDialog = false
                    resetSwipe = true
                },
                icon = {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                },
                title = { Text("Eliminar Evento") },
                text = {
                    Text("¿Estás seguro de que deseas eliminar el evento \"${event.tituloEvento}\"? Esta acción no se puede deshacer.")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            onDelete(event)
                            showDeleteDialog = false
                            resetSwipe = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) { Text("Eliminar") }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog = false
                            resetSwipe = true
                        }
                    ) { Text("Cancelar") }
                }
            )
        }

        if (isAdmin) {
            SwipeToDismissBox(
                state = dismissState,
                backgroundContent = { DeleteBackground(dismissState) },
                content = {
                    EventItem(
                        event = event,
                        currentUserEmail = currentUserEmail,
                        onClick = onClick
                    )
                },
                enableDismissFromStartToEnd = false,
                enableDismissFromEndToStart = true
            )
        } else {
            EventItem(
                event = event,
                currentUserEmail = currentUserEmail,
                onClick = onClick
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteBackground(dismissState: SwipeToDismissBoxState) {
    val color = when (dismissState.targetValue) {
        SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.error
        else -> Color.Transparent
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color, RoundedCornerShape(16.dp))
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Eliminar",
                    color = MaterialTheme.colorScheme.onError,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.onError
                )
            }
        }
    }
}

@Composable
fun EventActionDialog(
    event: Event,
    currentUserEmail: String?,
    onDismiss: () -> Unit,
    onAccept: (Event, String) -> Unit,
    onCancelAcceptance: (Event) -> Unit
) {
    val isAcceptedByCurrentUser = event.userAccept == currentUserEmail

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (isAcceptedByCurrentUser) "Cancelar Asistencia" else event.tituloEvento)
        },
        text = {
            Text(event.descripcionEvento)
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (isAcceptedByCurrentUser) {
                        onCancelAcceptance(event)
                    } else if (currentUserEmail != null) {
                        onAccept(event, currentUserEmail)
                    }
                    onDismiss()
                }
            ) {
                Text(if (isAcceptedByCurrentUser) "Confirmar Cancelación" else "Aceptar evento")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}

@Composable
fun ResolveEventDialog(
    event: Event,
    onDismiss: () -> Unit,
    onMarkAsResolved: (Event) -> Unit
) {
    if (event.resuelto) {
        LaunchedEffect(Unit) {
            onDismiss()
        }
        return
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Default.TaskAlt, contentDescription = "Resolver") },
        title = { Text("Gestionar Evento") },
        text = { Text("¿Deseas marcar el evento \"${event.tituloEvento}\" como solucionado?") },
        confirmButton = {
            Button(
                onClick = {
                    onMarkAsResolved(event)
                    onDismiss()
                }
            ) {
                Text("Sí, Solucionado")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun EventItem(
    event: Event,
    currentUserEmail: String?,
    onClick: () -> Unit
) {
    val isCreator = event.userId == currentUserEmail
    val isClickable = !isCreator && (event.userAccept == null || event.userAccept == currentUserEmail)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = isClickable) { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
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
                Text(
                    text = event.fechaCreacion.formatToString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            when {
                isCreator -> CreatorChip()
                event.userAccept != null -> {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Aceptado por",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = event.userAccept.substringBefore('@'),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                else -> StatusChip()
            }
        }
    }
}

@Composable
fun CreatorChip() {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.tertiaryContainer, RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = "TU EVENTO",
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

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
