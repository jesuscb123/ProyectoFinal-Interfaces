package dam2.jetpack.proyectofinal.auth.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dam2.jetpack.proyectofinal.auth.presentation.viewmodel.AuthViewModel

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    navcontroller: NavController
    ){
    val state by viewModel.uiState.collectAsState()
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(state.isAuthenticated) {
        if (state.isAuthenticated){
            navcontroller.navigate("home")
        }
    }

    //Email
   Column(
       modifier = Modifier.fillMaxSize(),
       verticalArrangement = Arrangement.Center,
       horizontalAlignment = Alignment.CenterHorizontally
   ){
       Credenciales(email) {
           email = it
       }

       Spacer(modifier = Modifier.padding(5.dp))

       // Password
       Credenciales(password) {
           password = it
       }

       Spacer(modifier = Modifier.padding(8.dp))

       if (!state.isLoading){
           Button(onClick = {
               viewModel.login(email, password)
           }){
               Text("Iniciar sesión")
           }
       }else{
           CircularProgressIndicator()

       }

       state.error?.let {msj ->
           Spacer(modifier = Modifier.padding(8.dp))
           Text(msj, color = Color.Red)
       }
   }



}

@Composable
fun Credenciales(credencial: String, onChangeCredencial: (String) -> Unit){
    OutlinedTextField(
        value = credencial,
        onValueChange = onChangeCredencial,
        placeholder = {
            Text(credencial)
        }
    )
}


