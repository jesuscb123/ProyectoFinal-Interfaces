package dam2.jetpack.proyectofinal.user.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import dam2.jetpack.proyectofinal.events.domain.model.Event
import dam2.jetpack.proyectofinal.events.presentation.viewModel.EventViewModel
import dam2.jetpack.proyectofinal.user.presentation.viewmodel.UserViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userViewModel: UserViewModel = hiltViewModel(),
    eventViewModel: EventViewModel = hiltViewModel()
) {
    val userState by userViewModel.uiState.collectAsState()
    val eventState by eventViewModel.uiState.collectAsState()

    val isAdmin = userState.user?.rol.toString() == "ADMIN"

    LaunchedEffect(Unit) {
        FirebaseAuth.getInstance().currentUser?.uid?.let {
            userViewModel.getUserByFirebaseUid(it)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
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
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 24.dp)
            )

            if (eventState.events.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay eventos disponibles",
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
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
                            currentUserEmail = userState.user?.email,
                            isAdmin = isAdmin,
                            onDelete = {
                                eventViewModel.deleteEvent(event.eventId!!)
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventItem(
    event: Event,
    currentUserEmail: String?,
    isAdmin: Boolean,
    onDelete: () -> Unit
) {
    var showNotAdminDialog by remember { mutableStateOf(false) }

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                if (isAdmin) {
                    onDelete()
                } else {
                    showNotAdminDialog = true
                }
                false
            } else {
                false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromEndToStart = true,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.error),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = "Eliminar",
                    color = MaterialTheme.colorScheme.onError,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 24.dp)
                )
            }
        }
    ) {
        EventCard(event, currentUserEmail)
    }

    if (showNotAdminDialog) {
        AlertDialog(
            onDismissRequest = { showNotAdminDialog = false },
            title = { Text("Acción no permitida") },
            text = { Text("Esta opción solo está disponible para administradores.") },
            confirmButton = {
                TextButton(onClick = { showNotAdminDialog = false }) {
                    Text("Aceptar")
                }
            }
        )
    }
}

@Composable
private fun EventCard(
    event: Event,
    currentUserEmail: String?
) {
    var isAccepted by rememberSaveable(event.eventId) { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isAccepted = true },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(event.tituloEvento, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            Text("Creado por: ${event.userId}")
            Spacer(Modifier.height(4.dp))
            Text("Fecha: ${event.fechaCreacion.formatToString()}")
            Spacer(Modifier.height(8.dp))

            if (isAccepted) {
                Text(
                    text = "Aceptado por: ${currentUserEmail ?: "ti"}",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                Text(
                    text = "PENDIENTE",
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
