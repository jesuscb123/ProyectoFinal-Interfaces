package dam2.jetpack.proyectofinal.user.presentation.screen
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
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

                        EventItem(
                            event = event,
                            currentUserEmail = currentUserEmail,
                            onClick = {

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
                    eventViewModel.markEventAsResolved(it)
                }
            )
        }
    }
}