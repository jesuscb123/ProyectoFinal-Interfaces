package dam2.jetpack.proyectofinal.user.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dam2.jetpack.proyectofinal.user.domain.model.Rol
import dam2.jetpack.proyectofinal.user.domain.model.User
import dam2.jetpack.proyectofinal.user.presentation.viewmodel.UserViewModel

/**
 * Pantalla que muestra una lista de todos los usuarios registrados en la aplicación.
 *
 * Esta pantalla está destinada a ser accesible solo por usuarios con rol de administrador.
 * Obtiene la lista de usuarios del [UserViewModel] y la muestra en una [LazyColumn].
 * Si no hay usuarios, muestra un estado vacío [EmptyState].
 *
 * @param userViewModel El ViewModel que gestiona el estado y la lógica de los usuarios.
 */
@Composable
fun AdminScreen(
    userViewModel: UserViewModel = hiltViewModel()
) {
    // Observa el estado del ViewModel.
    // Usaremos `uiState` como en HomeScreen.
    val userState by userViewModel.uiState.collectAsState()

    // Llama a `loadUsers` solo una vez cuando la pantalla se carga.
    LaunchedEffect(Unit) {
        userViewModel.loadUsers()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
        ) {
            // Revisa si hay usuarios. Si no, muestra el estado vacío.
            if (userState.users.isEmpty()) {
                // Puedes reutilizar el EmptyState de HomeScreen si lo haces público,
                // o crear uno específico para usuarios.
                EmptyState(
                    title = "No hay usuarios",
                    message = "Todavía no se han registrado usuarios en la aplicación."
                )
            } else {
                // Muestra la lista de usuarios.
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(
                        items = userState.users,
                        key = { it.firebaseUid } // Usamos el uid del usuario como clave única.
                    ) { user ->
                        UserListItem(user = user)
                    }
                }
            }
        }
    }
}

/**
 * Composable que representa un único elemento en la lista de usuarios.
 *
 * Muestra la información de un usuario, incluyendo su email y un icono que diferencia
 * a los administradores ([Rol.ADMIN]) de los usuarios normales.
 *
 * @param user El objeto [User] a mostrar.
 */
@Composable
fun UserListItem(
    user: User,
) {
    // Usamos el mismo estilo de Card que en EventItem.
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono que cambia si el usuario es ADMIN.
            val icon = if (user.rol == Rol.ADMIN) Icons.Default.AdminPanelSettings else Icons.Default.Person

            Icon(
                imageVector = icon,
                contentDescription = "Rol de usuario",
                modifier = Modifier
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape)
                    .padding(8.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Opcional: Chip para destacar el rol de Admin
            if (user.rol == Rol.ADMIN) {
                Spacer(modifier = Modifier.width(16.dp))
                AdminChip()
            }
        }
    }
}

/**
 * Un pequeño chip visual para identificar rápidamente a los usuarios administradores.
 *
 * Muestra la palabra "Admin" con un estilo destacado.
 */
@Composable
fun AdminChip() {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        Text(
            text = "Admin",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

/**
 * Composable que muestra un mensaje genérico para estados vacíos.
 *
 * Se utiliza cuando una lista o contenido no está disponible, mostrando un título y un mensaje
 * descriptivo para el usuario.
 *
 * @param modifier Modificador para personalizar el layout del [Box] contenedor.
 * @param title El título principal del mensaje.
 * @param message El mensaje secundario o descriptivo.
 */
@Composable
fun EmptyState(
    modifier: Modifier = Modifier,
    title: String,
    message: String
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}
