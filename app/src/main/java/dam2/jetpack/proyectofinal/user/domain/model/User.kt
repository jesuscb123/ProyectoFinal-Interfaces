package dam2.jetpack.proyectofinal.user.domain.model

/**
 * Modelo de datos que representa a un usuario en la capa de dominio.
 *
 * Esta clase encapsula la información esencial de un usuario, como su identificación,
 * email, rol y puntos, desacoplando la lógica de negocio de las implementaciones
 * específicas de la capa de datos.
 *
 * @property firebaseUid El ID único de Firebase que identifica al usuario.
 * @property email La dirección de correo electrónico del usuario.
 * @property rol El rol asignado al usuario (e.g., [Rol.USER], [Rol.ADMIN]).
 * @property puntos La cantidad de puntos que el usuario ha acumulado.
 */
data class User(
    val firebaseUid: String,
    val email: String,
    val rol: Rol,
    val puntos: Int = 0
) {
}
