package dam2.jetpack.proyectofinal.auth.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import dam2.jetpack.proyectofinal.R
import dam2.jetpack.proyectofinal.auth.presentation.viewmodel.AuthViewModel


/**
 * Pantalla de autenticación principal que permite a los usuarios iniciar sesión.
 *
 * Esta pantalla contiene campos para el correo electrónico y la contraseña, un botón de inicio de sesión,
 * y un enlace para navegar a la pantalla de registro. La interfaz de usuario reacciona al estado
 * (carga, éxito, error) proporcionado por el [AuthViewModel].
 *
 * @param viewModel El ViewModel [AuthViewModel] que gestiona la lógica de autenticación y el estado de la UI.
 * @param navcontroller El [NavController] utilizado para la navegación, por ejemplo, al iniciar sesión
 *                      correctamente o al ir a la pantalla de registro.
 */
@Composable
fun AuthScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    navcontroller: NavController
) {
    val state by viewModel.uiState.collectAsState()
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    // Efecto para navegar a la pantalla principal si la autenticación es exitosa
    LaunchedEffect(state.isAuthenticated) {
        if (state.isAuthenticated) {
            navcontroller.navigate("home") {
                popUpTo(navcontroller.graph.startDestinationId) { inclusive = true }
            }
        }
    }

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
                    .size(300.dp)
                    .padding(bottom = 24.dp)
            )

            AuthTextField(
                value = email,
                onValueChange = { email = it },
                label = "Correo Electrónico",
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(16.dp))

            AuthTextField(
                value = password,
                onValueChange = { password = it },
                label = "Contraseña",
                keyboardType = KeyboardType.Password,
                isPassword = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(50.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 5.dp
                )
            } else {
                LoginButton("iniciar sesión",
                    onClick = {
                        viewModel.login(email, password)
                    }
                )
            }

            state.error?.let { errorMessage ->
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            SingUp(navController = navcontroller)
        }
    }
}

/**
 * Un campo de texto personalizado y estilizado para la pantalla de autenticación.
 *
 * @param value El texto actual del campo.
 * @param onValueChange La función lambda que se invoca cuando el texto cambia.
 * @param label La etiqueta que se muestra dentro del campo de texto cuando está vacío.
 * @param keyboardType El tipo de teclado que se debe mostrar (ej. Email, Password).
 * @param isPassword Si es `true`, el texto se ofusca como una contraseña.
 */
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
        label = { Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedTextColor = MaterialTheme.colorScheme.onBackground,
            unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
            cursorColor = MaterialTheme.colorScheme.primary
        ),
        singleLine = true
    )
}

/**
 * Un botón de inicio de sesión personalizado con un fondo de gradiente y sombra.
 *
 * @param text El texto que se muestra en el botón.
 * @param onClick La función lambda que se ejecuta cuando se presiona el botón.
 */
@Composable
fun LoginButton(text: String, onClick: () -> Unit) {
    val gradient = Brush.horizontalGradient(
        listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.secondary
        )
    )
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * Un texto simple con un `clickable` que navega a la pantalla de registro.
 *
 * @param navController El [NavController] utilizado para navegar a la ruta "register".
 */
@Composable
fun SingUp(navController: NavController){
    Text("Registrarse", modifier = Modifier.clickable(
        onClick = {navController.navigate("register")}
    ))
}
