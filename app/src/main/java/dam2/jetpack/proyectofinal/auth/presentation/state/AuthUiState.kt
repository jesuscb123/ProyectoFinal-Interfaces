package dam2.jetpack.proyectofinal.auth.presentation.state

import dam2.jetpack.proyectofinal.user.domain.model.User

/**
 * Representa el estado de la interfaz de usuario para las pantallas de autenticación.
 *
 * Esta clase de datos inmutable contiene toda la información necesaria para renderizar
 * la UI de autenticación, incluyendo el estado de carga, si el usuario está autenticado,
 * los datos del usuario y cualquier mensaje de error.
 *
 * @property isLoading `true` si una operación de autenticación (login, registro) está en curso.
 *                     Esto se puede usar para mostrar un indicador de progreso.
 * @property isAuthenticated `true` si el usuario ha iniciado sesión correctamente.
 *                           Esto puede usarse para disparar la navegación a la pantalla principal.
 * @property user Los datos del [User] autenticado. Es `null` si el usuario no ha iniciado sesión
 *                o si los datos aún no se han cargado.
 * @property error Una cadena de texto que contiene un mensaje de error si una operación falló.
 *                 Es `null` si no hay errores.
 */
data class AuthUiState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val user: User? = null,
    val error: String? = null
)
