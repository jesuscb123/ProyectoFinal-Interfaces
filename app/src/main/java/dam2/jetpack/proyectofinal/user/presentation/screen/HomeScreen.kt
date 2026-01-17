package dam2.jetpack.proyectofinal.user.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import dam2.jetpack.proyectofinal.events.domain.model.Event
import dam2.jetpack.proyectofinal.events.presentation.viewModel.EventViewModel
import dam2.jetpack.proyectofinal.user.presentation.viewmodel.UserViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(
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
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background.copy(alpha = 0.9f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bienvenido ${userState.user?.email ?: "usuario"}",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 24.dp)
            )

            if (eventState.events.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 80.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No hay eventos disponibles",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = eventState.events,
                        key = { it.eventId!! }
                    ) { event ->
                        EventItem(
                            event = event,
                            onClick = {
                                selectedEvent = event
                            }
                        )
                    }
                }
            }
        }

        selectedEvent?.let { event ->
            val currentUserEmail = userState.user?.email
            val isAcceptedByCurrentUser = event.userAccept == currentUserEmail

            AlertDialog(
                onDismissRequest = { selectedEvent = null },
                // El título y el texto cambian según el estado
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
                            selectedEvent = null // Cerramos el diálogo en ambos casos
                        }
                    ) {

                        Text(if (isAcceptedByCurrentUser) "Confirmar Cancelación" else "Aceptar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { selectedEvent = null }) {
                        Text("Cerrar") // Un texto más genérico para el botón de cerrar
                    }
                }
            )
        }
    }
}

@Composable
fun EventItem(
    event: Event,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = event.userAccept == null || event.userAccept == FirebaseAuth.getInstance().currentUser?.email) { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = event.tituloEvento,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Creado por: ${event.userId}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Fecha: ${event.fechaCreacion.formatToString()}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (event.userAccept != null) {
                Text(
                    text = "Aceptado por: ${event.userAccept}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                Text(
                    text = "PENDIENTE",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

fun Date.formatToString(): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return formatter.format(this)
}
