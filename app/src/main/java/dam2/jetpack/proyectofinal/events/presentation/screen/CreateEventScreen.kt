package dam2.jetpack.proyectofinal.events.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dam2.jetpack.proyectofinal.events.domain.model.Category
import dam2.jetpack.proyectofinal.events.presentation.viewModel.EventViewModel

@Composable
fun CreateEvent(
    userId: String,
    eventViewModel: EventViewModel = hiltViewModel(),
    navController: NavController
){
    val eventState by eventViewModel.uiState.collectAsState()

    var tituloEvento by rememberSaveable { mutableStateOf("") }
    var descripcionEvento by rememberSaveable { mutableStateOf("") }
    var categoria by rememberSaveable { mutableStateOf(Category.COMUNIDAD) }
    var resuelto by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        OutlinedTextField(value = tituloEvento, onValueChange = {tituloEvento = it})
        OutlinedTextField(value = descripcionEvento, onValueChange = {descripcionEvento = it})
        if (eventState.isLoading){
            CircularProgressIndicator()
        }else{
            Button(onClick = {
                eventViewModel.createEvent(userId, tituloEvento, descripcionEvento, categoria, resuelto)
                navController.navigate("home")

            }){
                Text(text = "Crear evento")
            }
        }
    }


}
