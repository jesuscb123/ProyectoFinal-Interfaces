package dam2.jetpack.proyectofinal.user.presentation.state

import dam2.jetpack.proyectofinal.user.domain.model.User

data class UserStateUi(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null,
    val users: List<User> = emptyList()
)
