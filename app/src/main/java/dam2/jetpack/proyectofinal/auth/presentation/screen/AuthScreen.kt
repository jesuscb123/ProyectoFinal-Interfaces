package dam2.jetpack.proyectofinal.auth.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dam2.jetpack.proyectofinal.R // Importa R para el logo
import dam2.jetpack.proyectofinal.auth.presentation.viewmodel.AuthViewModel

// --- Paleta de colores inspirada en un logo de eventos (naranjas y azules) ---
val DarkBlue = Color(0xFF0D1B2A)
val LightBlue = Color(0xFF778DA9)
val OrangeStart = Color(0xFFF77F00)
val OrangeEnd = Color(0xFFFCBF49)
val ErrorRed = Color(0xFFD00000)
val TextLight = Color(0xFFE0E1DD)


@Composable
fun AuthScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    navcontroller: NavController
) {
    // --- LÓGICA ORIGINAL (SIN CAMBIOS) ---
    val state by viewModel.uiState.collectAsState()
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(state.isAuthenticated) {
        if (state.isAuthenticated) {
            navcontroller.navigate("home") {
                // Limpia el backstack para que el usuario no pueda volver a la pantalla de login
                popUpTo(navcontroller.graph.startDestinationId) { inclusive = true }
            }
        }
    }

    // --- NUEVO DISEÑO ---
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        DarkBlue.copy(alpha = 0.9f),
                        DarkBlue
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
            // --- LOGO ---
            Image(
                painter = painterResource(id = R.drawable.logoapp),
                contentDescription = "Logo de la App",
                modifier = Modifier
                    .size(300.dp)
                    .padding(bottom = 24.dp)
            )

            // --- CAMPO DE EMAIL ---
            AuthTextField(
                value = email,
                onValueChange = { email = it },
                label = "Correo Electrónico",
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- CAMPO DE CONTRASEÑA ---
            AuthTextField(
                value = password,
                onValueChange = { password = it },
                label = "Contraseña",
                keyboardType = KeyboardType.Password,
                isPassword = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- BOTÓN DE LOGIN O INDICADOR DE CARGA ---
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(50.dp),
                    color = OrangeStart,
                    strokeWidth = 5.dp
                )
            } else {
                LoginButton(
                    onClick = {
                        viewModel.login(email, password)
                    }
                )
            }

            // --- MENSAJE DE ERROR ---
            state.error?.let { errorMessage ->
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = errorMessage,
                    color = ErrorRed,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label, color = LightBlue) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = OrangeStart,
            unfocusedBorderColor = LightBlue,
            focusedTextColor = TextLight,
            unfocusedTextColor = TextLight,
            cursorColor = OrangeStart
        ),
        singleLine = true
    )
}

@Composable
fun LoginButton(onClick: () -> Unit) {
    val gradient = Brush.horizontalGradient(listOf(OrangeStart, OrangeEnd))
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues() // Importante para que el fondo del botón sea el degradado
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Iniciar Sesión",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
