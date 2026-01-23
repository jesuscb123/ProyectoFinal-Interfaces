package dam2.jetpack.proyectofinal.user.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import dam2.jetpack.proyectofinal.user.domain.model.Rol

/**
 * Representa la entidad de un usuario en la base de datos local Room.
 *
 * Esta clase de datos define la estructura de la tabla `users` y se utiliza para
 * mapear los objetos de usuario a registros en la base de datos.
 *
 * @property firebaseUid El ID único de Firebase del usuario. Actúa como clave primaria (@PrimaryKey).
 * @property email La dirección de correo electrónico del usuario.
 * @property rol El rol del usuario dentro de la aplicación (e.g., [Rol.ADMIN], [Rol.USER]).
 * @property puntos La cantidad de puntos acumulados por el usuario.
 */
@Entity(
    tableName = "users",
)
data class UserEntity(
    @PrimaryKey
    val firebaseUid: String,
    val email: String,
    val rol: Rol,
    val puntos: Int = 0
)
