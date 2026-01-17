package dam2.jetpack.proyectofinal.user.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import dam2.jetpack.proyectofinal.events.domain.model.Event
import dam2.jetpack.proyectofinal.events.presentation.viewModel.EventViewModel

@Composable
fun EventsUserScreen(
    eventViewModel: EventViewModel = hiltViewModel()
) {
    var acceptedEvents by remember { mutableStateOf<List<Event>?>(null) }

    val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email

    LaunchedEffect(key1 = currentUserEmail) {
        currentUserEmail?.let { email ->
            eventViewModel.getEventsUser(email).collect { events ->
                // Actualizamos nuestro estado local con los datos correctos.
                acceptedEvents = events
            }
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        when {
            acceptedEvents == null -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            // Caso 2: Hemos recibido la lista y está vacía.
            acceptedEvents!!.isEmpty() -> {
                EmptyState()
            }
            // Caso 3: Tenemos eventos para mostrar.
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
                ) {
                    items(
                        items = acceptedEvents!!, // Usamos la lista local
                        key = { it.eventId!! }
                    ) { event ->
                        EventItem(
                            event = event,
                            currentUserEmail = currentUserEmail,
                            onClick = { /* Lógica de clic si es necesaria */ }
                        )
                    }
                }
            }
        }
    }
}
