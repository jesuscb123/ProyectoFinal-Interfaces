package dam2.jetpack.proyectofinal.user.presentation.state

import dam2.jetpack.proyectofinal.user.domain.model.User

/**
 * Clase de estado que representa el estado de la UI relacionado con los usuarios.
 *
 * Esta clase de datos encapsula toda la información necesaria para renderizar las pantallas
 * que dependen de datos de usuario, como el estado de carga, el usuario actual,
 * una lista de usuarios (para vistas de administrador) y posibles errores.
 *
 * @property isLoading `true` si se está realizando una operación de carga (p. ej., fetching de datos), `false` en caso contrario.
 * @property user El objeto [User] del usuario actualmente autenticado. Puede ser `null` si no hay nadie logueado o si aún no se ha cargado.
 * @property error Una cadena de texto que contiene un mensaje de error si alguna operación falló. Es `null` si no hay errores.
 * @property users Una lista de objetos [User], utilizada principalmente en pantallas de administrador para mostrar todos los usuarios. Por defecto, es una lista vacía.
 */
data class UserStateUi(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null,
    val users: List<User> = emptyList()
)
