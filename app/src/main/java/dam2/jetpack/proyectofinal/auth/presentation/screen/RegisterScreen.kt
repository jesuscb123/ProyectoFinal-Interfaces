package dam2.jetpack.proyectofinal.auth.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dam2.jetpack.proyectofinal.R
import dam2.jetpack.proyectofinal.auth.presentation.viewmodel.AuthViewModel
import dam2.jetpack.proyectofinal.user.domain.model.Rol

@Composable
fun RegisterScreen(authViewModel: AuthViewModel = hiltViewModel(), navController: NavController) {
    // --- LÓGICA ORIGINAL (SIN CAMBIOS) ---
    val authState by authViewModel.uiState.collectAsState()
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var rol by rememberSaveable { mutableStateOf(Rol.USER) }

    LaunchedEffect(authState.isAuthenticated) {
        if (authState.isAuthenticated){
            // Navega a la pantalla de login tras un registro exitoso
            navController.navigate("auth") {
                // Limpiamos el backstack para que no pueda volver a registrarse con el botón "atrás"
                popUpTo("auth") { inclusive = true }
            }
        }
    }

    // --- NUEVO DISEÑO APLICADO ---
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background.copy(alpha = 0.9f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logoaap),
                contentDescription = "Logo de la App",
                modifier = Modifier
                    .size(250.dp) // Un poco más pequeño para dejar espacio a los RadioButton
                    .padding(bottom = 24.dp)
            )

            // --- CAMPO DE EMAIL (REUTILIZADO DE AUTHSCREEN) ---
            AuthTextField(
                value = email,
                onValueChange = { email = it },
                label = "Correo Electrónico",
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- CAMPO DE CONTRASEÑA (REUTILIZADO DE AUTHSCREEN) ---
            AuthTextField(
                value = password,
                onValueChange = { password = it },
                label = "Contraseña",
                keyboardType = KeyboardType.Password,
                isPassword = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- SELECCIÓN DE ROL (DISEÑO ADAPTADO) ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                RoleRadioButton(
                    text = "Usuario",
                    selected = rol == Rol.USER,
                    onClick = { rol = Rol.USER }
                )
                Spacer(modifier = Modifier.padding(16.dp))
                RoleRadioButton(
                    text = "Admin",
                    selected = rol == Rol.ADMIN,
                    onClick = { rol = Rol.ADMIN }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- BOTÓN DE REGISTRO O INDICADOR DE CARGA ---
            if (authState.isLoading){
                CircularProgressIndicator(
                    modifier = Modifier.size(50.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 5.dp
                )
            } else {
                // Reutilizamos LoginButton pero con el texto y la acción de registrar
                LoginButton(
                    onClick = { authViewModel.register(email, password, rol) },
                    text = "Registrarse"
                )
            }

            // --- MENSAJE DE ERROR ---
            authState.error?.let { errorMessage ->
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * Un RadioButton con texto, estilizado para que coincida con el tema.
 */
@Composable
private fun RoleRadioButton(text: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable(onClick = onClick) // Hace que toda la fila sea clickeable
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary,
                unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        Text(
            text = text,
            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}
