package dam2.jetpack.proyectofinal.events.presentation

import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.hilt.navigation.compose.hiltViewModel
import dam2.jetpack.proyectofinal.events.domain.model.Category
import dam2.jetpack.proyectofinal.events.presentation.viewModel.EventViewModel

@Composable
fun CreateEvent(
    userId: String,
    eventViewModel: EventViewModel = hiltViewModel(),
){
    val eventState by eventViewModel.uiState.collectAsState()

    val tituloEvento by rememberSaveable { mutableStateOf("") }
    val descripcionEvento by rememberSaveable { mutableStateOf("") }
    val categoria by rememberSaveable { mutableStateOf(Category.COMUNIDAD) }
    val fechaCreacion by rememberSaveable { mutableStateOf("") }
    val resuelto by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(value = tituloEvento, onValueChange = {tituloEvento})
    OutlinedTextField(value = descripcionEvento, onValueChange = {descripcionEvento})







}
