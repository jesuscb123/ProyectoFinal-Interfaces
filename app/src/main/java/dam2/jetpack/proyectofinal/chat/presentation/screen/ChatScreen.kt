package dam2.jetpack.proyectofinal.chat.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import dam2.jetpack.proyectofinal.chat.domain.model.ChatMessage
import dam2.jetpack.proyectofinal.chat.presentation.viewmodel.ChatViewModel
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(
    recipientUid: String,
    viewModel: ChatViewModel = hiltViewModel()
) {
    // Observa los mensajes del ViewModel como un estado de Compose
    val messages by viewModel.messages.collectAsState()
    val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid

    // Estado para el texto que el usuario está escribiendo
    var text by remember { mutableStateOf("") }

    // Estado para controlar el scroll de la lista de mensajes
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Este efecto se ejecuta una vez cuando la pantalla se carga, para obtener los mensajes
    LaunchedEffect(recipientUid) {
        viewModel.loadMessages(recipientUid)
    }

    // Este efecto se ejecuta cada vez que la lista de mensajes cambia
    LaunchedEffect(messages) {
        // Si hay mensajes, hace scroll automático hacia el más reciente (que está en el índice 0)
        if (messages.isNotEmpty()) {
            coroutineScope.launch {
                listState.animateScrollToItem(0)
            }
        }
    }

    Scaffold(
        // La barra inferior donde se escribe el mensaje
        bottomBar = {
            MessageInput(
                value = text,
                onValueChange = { text = it },
                onSendClick = {
                    if (text.isNotBlank()) {
                        viewModel.sendMessage(text.trim(), recipientUid)
                        text = "" // Limpia el campo de texto después de enviar
                    }
                }
            )
        }
    ) { paddingValues ->
        // La lista que muestra los mensajes
        LazyColumn(
            state = listState,
            reverseLayout = true, // Esencial para un chat: empieza desde abajo
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 8.dp)
        ) {
            items(messages) { message ->
                MessageBubble(
                    message = message,
                    isFromCurrentUser = message.senderId == currentUserUid
                )
            }
        }
    }
}

@Composable
fun MessageBubble(message: ChatMessage, isFromCurrentUser: Boolean) {
    // Determina la alineación y colores según si el mensaje es del usuario actual o del receptor
    val alignment = if (isFromCurrentUser) Alignment.CenterEnd else Alignment.CenterStart
    val backgroundColor = if (isFromCurrentUser) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.secondaryContainer
    }
    val textColor = if (isFromCurrentUser) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSecondaryContainer
    }

    // Un Box para alinear la burbuja a la derecha o izquierda
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        contentAlignment = alignment
    ) {
        // La "burbuja" del mensaje
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = backgroundColor,
            modifier = Modifier.widthIn(max = 300.dp) // Limita el ancho máximo de la burbuja
        ) {
            Text(
                text = message.text,
                color = textColor,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            )
        }
    }
}

@Composable
fun MessageInput(
    value: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    Surface(shadowElevation = 8.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Campo de texto para escribir el mensaje
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Escribe un mensaje...") },
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )
            Spacer(modifier = Modifier.width(8.dp))

            // Botón para enviar el mensaje
            IconButton(
                onClick = onSendClick,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                // El botón está deshabilitado si no hay texto
                enabled = value.isNotBlank()
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Enviar mensaje")
            }
        }
    }
}
