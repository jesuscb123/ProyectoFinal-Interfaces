package dam2.jetpack.proyectofinal.events.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dam2.jetpack.proyectofinal.events.domain.model.Category
import dam2.jetpack.proyectofinal.events.presentation.viewModel.EventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEvent(
    userId: String?,
    eventViewModel: EventViewModel = hiltViewModel(),
    navController: NavController
) {
    val eventState by eventViewModel.uiState.collectAsState()

    var tituloEvento by rememberSaveable { mutableStateOf("") }
    var descripcionEvento by rememberSaveable { mutableStateOf("") }
    var categoria by rememberSaveable { mutableStateOf(Category.COMUNIDAD) }
    val resuelto = false

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        // 1. Título del evento
        OutlinedTextField(
            value = tituloEvento,
            onValueChange = { tituloEvento = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Título del evento") },
            leadingIcon = { Icon(Icons.Default.Title, contentDescription = "Título") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = descripcionEvento,
            onValueChange = { descripcionEvento = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            label = { Text("Descripción (opcional)") },
            leadingIcon = { Icon(Icons.Default.Description, contentDescription = "Descripción") }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Selecciona una categoría", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))

        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                onClick = { categoria = Category.COMUNIDAD },
                selected = categoria == Category.COMUNIDAD
            ) {
                Text("Comunidad")
            }
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                onClick = { categoria = Category.PERSONAL },
                selected = categoria == Category.PERSONAL
            ) {
                Text("Personal")
            }
        }

        // Este Spacer empuja el botón hacia la parte inferior de la pantalla
        Spacer(modifier = Modifier.weight(1f))

        // 4. Botón de creación
        Button(
            onClick = {
                // La lógica que ya tenías
                eventViewModel.createEvent(userId, tituloEvento, descripcionEvento, categoria, resuelto)
                navController.navigate("home")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            // El botón se deshabilita si no hay título o si ya se está cargando
            enabled = tituloEvento.isNotBlank() && !eventState.isLoading
        ) {
            if (eventState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary, // Color del spinner
                    strokeWidth = 3.dp
                )
            } else {
                Text("Crear Evento", fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
