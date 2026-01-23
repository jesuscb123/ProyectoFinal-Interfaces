package dam2.jetpack.proyectofinal.core.components.navigation

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import dam2.jetpack.proyectofinal.events.domain.model.Event
import dam2.jetpack.proyectofinal.user.presentation.screen.AcceptedChip
import dam2.jetpack.proyectofinal.user.presentation.screen.CreatorChipV2
import dam2.jetpack.proyectofinal.user.presentation.screen.InfoPill
import dam2.jetpack.proyectofinal.user.presentation.screen.PendingChipV2
import dam2.jetpack.proyectofinal.user.presentation.screen.formatToString
import dam2.jetpack.proyectofinal.user.presentation.screen.toIcon

@Composable
fun EventItem(
    event: Event,
    currentUserEmail: String?,
    navController: NavController,
    onClick: () -> Unit
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val currentUserUid = currentUser?.uid

    // Mejor comprobar creador por UID (más fiable que por email)
    val isCreator = (event.creatorUid == currentUserUid)

    // Evento aceptado por alguien
    val isAcceptedBySomeone = event.userAcceptUid != null

    // Puede chatear si: hay aceptador y soy creador o soy el aceptador
    val canChat = isAcceptedBySomeone && currentUserUid != null &&
            (isCreator || event.userAcceptUid == currentUserUid)

    // Click del card: tu lógica original (solo no creador y si está libre o lo aceptó él)
    val isClickable = !isCreator && (event.userAccept == null || event.userAccept == currentUserEmail)

    val scheme = MaterialTheme.colorScheme
    val container = scheme.surface
    val headerBg = scheme.primaryContainer
    val headerFg = scheme.onPrimaryContainer

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = isClickable, onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = container),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column {
            // --- Cabecera ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = headerBg)
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .background(scheme.secondaryContainer, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = event.categoria.toIcon(),
                            contentDescription = "Categoría",
                            tint = scheme.onSecondaryContainer,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = event.tituloEvento,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = headerFg,
                            maxLines = 1
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "por ${event.userId.substringBefore('@')}",
                            style = MaterialTheme.typography.bodySmall,
                            color = headerFg.copy(alpha = 0.85f),
                            maxLines = 1
                        )
                    }

                    when {
                        isCreator -> CreatorChipV2()
                        event.userAccept != null -> AcceptedChip(userAccept = event.userAccept)
                        else -> PendingChipV2()
                    }
                }
            }

            // --- Cuerpo ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = event.descripcionEvento,
                    style = MaterialTheme.typography.bodyMedium,
                    color = scheme.onSurface,
                    maxLines = 2
                )

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    InfoPill(
                        icon = Icons.Default.CalendarMonth,
                        text = event.fechaCreacion.formatToString()
                    )
                    InfoPill(
                        icon = Icons.Default.Category,
                        text = event.categoria.name.lowercase().replaceFirstChar { it.uppercase() }
                    )
                }

                // --- Botón Chat (visible para creador y aceptador) ---
                if (canChat) {
                    Spacer(Modifier.height(16.dp))
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = {
                                val recipientUid: String
                                val recipientEmail: String

                                if (isCreator) {
                                    // Soy el creador: chateo con el aceptador
                                    recipientUid = event.userAcceptUid!!
                                    recipientEmail = event.userAccept ?: "usuario"
                                } else {
                                    // Soy el aceptador: chateo con el creador
                                    recipientUid = event.creatorUid
                                    recipientEmail = event.userId
                                }

                                navController.navigate(
                                    "chat?eventId=${Uri.encode(event.eventId.toString())}" +
                                            "&recipientUid=${Uri.encode(recipientUid)}" +
                                            "&recipientEmail=${Uri.encode(recipientEmail)}"
                                )
                            },
                            modifier = Modifier.align(Alignment.CenterEnd)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Chat,
                                contentDescription = "Chat",
                                modifier = Modifier.size(ButtonDefaults.IconSize)
                            )
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text("Chatear")
                        }
                    }
                }
            }
        }
    }
}