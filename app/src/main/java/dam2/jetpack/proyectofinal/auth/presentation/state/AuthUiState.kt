package dam2.jetpack.proyectofinal.auth.presentation.state

import dam2.jetpack.proyectofinal.user.domain.model.User

data class AuthUiState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val user: User? = null,
    val error: String? = null
)
