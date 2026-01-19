package dam2.jetpack.proyectofinal.user.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import dam2.jetpack.proyectofinal.user.domain.model.Rol

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