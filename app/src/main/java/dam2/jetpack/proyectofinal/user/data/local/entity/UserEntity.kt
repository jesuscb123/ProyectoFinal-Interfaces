package dam2.jetpack.proyectofinal.user.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
)
data class UserEntity(
    @PrimaryKey
    val firebaseUid: String,
    val email: String,
    val nombreUsuario: String,
    val rol: String
)