package dam2.jetpack.proyectofinal.chat.presentation.screen

import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import dam2.jetpack.proyectofinal.chat.domain.model.ChatMessage
import dam2.jetpack.proyectofinal.chat.presentation.viewmodel.ChatViewModel
import kotlinx.coroutines.launch

/**
 * Pantalla principal para una conversación de chat entre dos usuarios dentro de un evento.
 *
 * Muestra una lista de mensajes y un campo de entrada para enviar nuevos mensajes.
 * La pantalla carga los mensajes existentes al entrar y escucha actualizaciones en tiempo real.
 *
 * @param eventId El ID del evento al que pertenece el chat.
 * @param recipientUid El UID del usuario destinatario.
 * @param recipientEmail El email del destinatario, que puede usarse para mostrar en la UI.
 * @param navController El controlador de navegación para acciones como volver atrás.
 * @param viewModel El [ChatViewModel] que gestiona la lógica y el estado del chat.
 */
@OptIn(ExperimentalMaterial3Api::class) // Necesario para TopAppBar
@Composable
fun ChatScreen(
    eventId: String,
    recipientUid: String,
    recipientEmail: String, 
    navController: NavController,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val messages by viewModel.messages.collectAsState()
    val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid

    var text by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    val speechRecognizerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
            ) { result ->
        // Cuando el reconocedor de voz termina, este bloque se ejecuta
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val data: Intent? = result.data
            val results = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            // Actualiza el campo de texto con el resultado de la transcripción
            text = results?.get(0) ?: ""
        }
    }

    val onMicClick = {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla ahora...")
        }
        speechRecognizerLauncher.launch(intent)
    }

    // Listener para cargar mensajes
    DisposableEffect(eventId, recipientUid) {
        viewModel.loadMessages(eventId, recipientUid)
        onDispose { viewModel.stopListening() }
    }

    // Scroll al último mensaje (en reverseLayout, es el ítem 0)
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Lista de mensajes
        LazyColumn(
            state = listState,
            reverseLayout = true,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            items(
                items = messages,
                key = { "${it.senderId}_${it.timestamp?.seconds ?: 0}_${it.timestamp?.nanoseconds ?: 0}_${it.text.hashCode()}" }
            ) { message ->
                MessageBubble(
                    message = message,
                    isFromCurrentUser = message.senderId == currentUserUid
                )
            }
        }

        // Input abajo fijo
        MessageInput(
            value = text,
            onValueChange = { text = it },
            onSendClick = {
                val trimmed = text.trim()
                if (trimmed.isNotBlank()) {
                    viewModel.sendMessage(eventId, trimmed, recipientUid)
                    text = ""
                }
            },
            onMicClick = onMicClick
        )
    }
}

/**
 * Composable que representa una burbuja de mensaje individual en el chat.
 *
 * Adapta su alineación y color de fondo dependiendo de si el mensaje es del
 * usuario actual o del otro participante.
 *
 * @param message El objeto [ChatMessage] a mostrar.
 * @param isFromCurrentUser `true` si el mensaje fue enviado por el usuario actual, `false` en caso contrario.
 */
@Composable
fun MessageBubble(message: ChatMessage, isFromCurrentUser: Boolean) {
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

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        contentAlignment = alignment
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = backgroundColor,
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Text(
                text = message.text,
                color = textColor,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            )
        }
    }
}

/**
 * Composable que representa el campo de entrada de texto y el botón de enviar.
 *
 * @param value El texto actual en el campo de entrada.
 * @param onValueChange Callback que se invoca cuando el texto cambia.
 * @param onSendClick Callback que se invoca cuando se presiona el botón de enviar.
 */
@Composable
fun MessageInput(
    value: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit,
    onMicClick: () -> Unit
) {
    Surface(shadowElevation = 8.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Escribe un mensaje...") },
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                ),
                trailingIcon = {
                    IconButton(onClick = onMicClick) {
                        Icon(
                            Icons.Default.Mic,
                            contentDescription = "Transcribir voz a texto"
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = onSendClick,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                enabled = value.isNotBlank()
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Enviar mensaje")
            }
        }
    }
}
