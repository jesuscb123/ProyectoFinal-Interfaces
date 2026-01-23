package dam2.jetpack.proyectofinal.user.presentation.screen

import android.util.Log
import androidx.compose.animation.core.copy
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.error
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.core.text.color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import dam2.jetpack.proyectofinal.events.domain.model.Event
import dam2.jetpack.proyectofinal.events.presentation.viewModel.EventViewModel
import java.text.SimpleDateFormat
import kotlin.text.format

/**
 * Pantalla que muestra los eventos en los que el usuario actual está participando (ha aceptado).
 *
 * Esta pantalla obtiene y muestra una lista de eventos que el usuario ha aceptado ayudar.
 * Cada evento se muestra en una tarjeta con una animación de entrada. Al hacer clic en un evento,
 * se abre un diálogo [EventActionDialog] que permite al usuario cancelar su participación.
 * Si el usuario no participa en ningún evento, se muestra un estado vacío [EmptyState].
 *
 * @param eventViewModel El ViewModel que gestiona la lógica y el estado de los eventos.
 * @param navController El controlador de navegación para gestionar las transiciones entre pantallas.
 */
@Composable
fun EventsUserScreen(
    eventViewModel: EventViewModel = hiltViewModel(),
    navController: NavController
) {
    val eventState by eventViewModel.uiState.collectAsState()
    val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email
    var selectedEvent by remember { mutableStateOf<Event?>(null) }

    LaunchedEffect(key1 = currentUserEmail) {
        currentUserEmail?.let {
            Log.d("EventsUserScreen", "Filtrando eventos para el usuario: ${"$"}currentUserEmail")
            eventViewModel.getEventsUser(it)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (eventState.events.isEmpty()) {
            EmptyState(
                title = "No participas en ningún evento",
                message = "Cuando aceptes un evento, aparecerá aquí."
            )
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
                    var isVisible by remember { mutableStateOf(false) }
                    LaunchedEffect(key1 = true) { isVisible = true }

                    androidx.compose.animation.AnimatedVisibility(
                        visible = isVisible,
                        enter = androidx.compose.animation.fadeIn(
                            animationSpec = androidx.compose.animation.core.tween(500)
                        ) + androidx.compose.animation.slideInVertically(
                            initialOffsetY = { it / 2 },
                            animationSpec = androidx.compose.animation.core.tween(500)
                        ),
                        exit = androidx.compose.animation.fadeOut() +
                                androidx.compose.animation.shrinkVertically()
                    ) {
                        // ✅ Reutiliza el mismo EventItem "vistoso" del HomeScreen
                        EventItem(
                            event = event,
                            currentUserEmail = currentUserEmail,
                            navController = navController,
                            onClick = { selectedEvent = event }
                        )
                    }
                }
            }
        }

        selectedEvent?.let { event ->
            EventActionDialog(
                event = event,
                currentUserEmail = currentUserEmail,
                onDismiss = { selectedEvent = null },
                onAccept = { evt, email ->
                    eventViewModel.acceptEvent(evt, email)
                },
                onCancelAcceptance = { evt ->
                    eventViewModel.cancelAcceptance(evt)
                }
            )
        }
    }
}
