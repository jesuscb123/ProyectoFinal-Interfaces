package dam2.jetpack.proyectofinal.user.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import dam2.jetpack.proyectofinal.events.presentation.viewModel.EventViewModel

@Composable
fun MyEventsScreen(
    eventViewModel: EventViewModel = hiltViewModel()
) {
    val eventState by eventViewModel.uiState.collectAsState()
    val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email

    LaunchedEffect(key1 = currentUserEmail) {
        currentUserEmail?.let {
            eventViewModel.getEventsUser(it)
        }
    }

    if (eventState.events.isEmpty()) {
        EmptyState()
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
        ) {
            items(
                items = eventState.events,
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
