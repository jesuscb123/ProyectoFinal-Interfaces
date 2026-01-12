package dam2.jetpack.proyectofinal.user.data.mapper

import dam2.jetpack.proyectofinal.user.data.local.entity.UserEntity
import dam2.jetpack.proyectofinal.user.domain.model.Rol
import dam2.jetpack.proyectofinal.user.domain.model.User

fun UserEntity.toDomain() = User(
    firebaseUid = firebaseUid,
    email = email,
    rol = Rol.valueOf(rol),
    nombreUsuario = nombreUsuario
)

fun User.toEntity() = UserEntity(
    firebaseUid = firebaseUid,
    email = email,
    rol = rol.name,
    nombreUsuario = nombreUsuario
)
