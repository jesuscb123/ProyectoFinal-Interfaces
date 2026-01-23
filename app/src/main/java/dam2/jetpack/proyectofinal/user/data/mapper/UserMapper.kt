package dam2.jetpack.proyectofinal.user.data.mapper

import dam2.jetpack.proyectofinal.user.data.local.entity.UserEntity
import dam2.jetpack.proyectofinal.user.domain.model.Rol
import dam2.jetpack.proyectofinal.user.domain.model.User

/**
 * Fichero que contiene las funciones de extensión para mapear entre las entidades
 * de la capa de datos ([UserEntity]) y los modelos de la capa de dominio ([User]).
 *
 * Este enfoque permite mantener las capas de la arquitectura limpia separadas
 * y facilita la conversión de objetos entre ellas.
 */

/**
 * Convierte un objeto de entidad de Room [UserEntity] a un modelo de dominio [User].
 *
 * @return Una instancia de [User] con los mismos datos.
 */
fun UserEntity.toDomain() = User(
    firebaseUid = firebaseUid,
    email = email,
    rol = rol,
    puntos = puntos
)

/**
 * Convierte un modelo de dominio [User] a un objeto de entidad de Room [UserEntity].
 *
 * @return Una instancia de [UserEntity] lista para ser almacenada en la base de datos.
 */
fun User.toEntity() = UserEntity(
    firebaseUid = firebaseUid,
    email = email,
    rol = rol,
    puntos = puntos
)
