package dam2.jetpack.proyectofinal.auth.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dam2.jetpack.proyectofinal.auth.presentation.viewmodel.AuthViewModel
import dam2.jetpack.proyectofinal.user.domain.model.Rol

@Composable
fun RegisterScreen(authViewModel: AuthViewModel = hiltViewModel(), navController: NavController) {
    val authState by authViewModel.uiState.collectAsState()
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var rol by rememberSaveable { mutableStateOf(Rol.USER) }


    LaunchedEffect(authState) {
        if (authState.isAuthenticated){
            navController.navigate("auth")
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(value = email, onValueChange = {email = it})
        Spacer(modifier = Modifier.padding(8.dp))
        OutlinedTextField(value = password, onValueChange = {password = it})
        Spacer(modifier = Modifier.padding(8.dp))
       if (!authState.isLoading){
           Button(onClick = {
               authViewModel.register(email, password, rol)
           }) {"Registro"}
       }else{
           CircularProgressIndicator()
       }

    }
}
