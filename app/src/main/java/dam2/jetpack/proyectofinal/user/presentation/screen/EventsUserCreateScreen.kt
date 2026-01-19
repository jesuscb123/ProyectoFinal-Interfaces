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
import androidx.compose.material.icons.filled.CheckCircle
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

@Composable
fun EventItemCreated(
    event: Event,
    onResolveClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = event.categoria.toIcon(),
                    contentDescription = "Categoría",
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            MaterialTheme.colorScheme.secondaryContainer,
                            CircleShape
                        )
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
            }

            // --- PARTE INFERIOR (LÓGICA DE ACCIONES) ---
            Spacer(modifier = Modifier.height(16.dp))

            when {
                // 1. Si el evento ya está resuelto
                event.resuelto -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = "Solucionado", tint = Color(0xFF00C853))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "SOLUCIONADO",
                            color = Color(0xFF00C853),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                // 2. Si alguien lo ha aceptado (y no está resuelto)
                event.userAccept != null -> {
                    Button(
                        onClick = onResolveClick, // Aquí se activa el diálogo
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !event.resuelto // El botón está activo solo si no está resuelto
                    ) {
                        Text("MARCAR COMO SOLUCIONADO")
                    }
                }
                // 3. Si nadie lo ha aceptado todavía
                else -> {
                    Text(
                        text = "Pendiente de aceptación por otro usuario",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}