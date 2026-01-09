package dam2.jetpack.proyectofinal.user.domain.model

data class User(
    val firebaseUid: String,
    val email: String,
    val nombreUsuario: String,
    val rol: Rol
) {
}