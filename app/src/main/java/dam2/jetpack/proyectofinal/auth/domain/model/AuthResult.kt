package dam2.jetpack.proyectofinal.auth.domain.model

import dam2.jetpack.proyectofinal.user.domain.model.Rol

data class AuthResult(
    val uid: String,
    val email: String?
)
